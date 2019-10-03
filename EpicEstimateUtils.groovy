import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.link.IssueLink
import com.atlassian.jira.issue.link.IssueLinkManager
import com.atlassian.jira.issue.link.IssueLinkType
import com.atlassian.jira.issue.link.IssueLinkTypeManager
import SumType


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class EpicEstimateUtils {

	Issue mIssue;
	String mRED   = "#ff0000"
	String mGREEN = "#009900"
	String mAMBER = "#cc7700"
	String mUNKNOWN = "#333333"

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def EpicEstimateUtils(Issue issue) {
		mIssue = issue
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def Long getOriginalEstimateInSeconds() {
		return getEpicTotalForTypeInSeconds(SumType.ORIGINAL)
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def Long getRemainingEstimateInSeconds() {
		return getEpicTotalForTypeInSeconds(SumType.REMAINING)
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def Long getLoggedWorkInSeconds() {
		return getEpicTotalForTypeInSeconds(SumType.LOGGED)
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def Long getEpicTotalForTypeInSeconds(SumType SumType) {
		Long total = 0
		IssueLinkTypeManager issueLinkTypeManager = ComponentAccessor.getComponent(IssueLinkTypeManager)
		IssueLinkManager issueLinkManager = ComponentAccessor.issueLinkManager
		Collection<IssueLinkType> storyLinkTypes = issueLinkTypeManager.getIssueLinkTypesByName('Epic-Story Link')

		if (mIssue.issueTypeObject.name == "Epic") {
			if (storyLinkTypes) {
				Long storyLinkTypeId = storyLinkTypes[0].id
				List<IssueLink> issueLinks = issueLinkManager.getOutwardLinks(mIssue.id).findAll{it.linkTypeId==storyLinkTypeId}
				if (issueLinks) {
					issueLinks.each {
						total = total + getIssueValueForTypeInSeconds(it.getDestinationObject(), SumType)
						total = total + getSubTaskTotalForTypeInSeconds(it.getDestinationObject(), SumType)
					}

					return total
				}
			}
		}

		return null
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def Long getIssueValueForTypeInSeconds(Issue issue, SumType SumType) {
		if (SumType == SumType.ORIGINAL) {
			return issue.getOriginalEstimate() ? issue.getOriginalEstimate() : 0
		} else if (SumType == SumType.REMAINING) {
			return issue.getEstimate() ? issue.getEstimate() : 0
		} else if (SumType == SumType.LOGGED) {
			return issue.getTimeSpent() ? issue.getTimeSpent() : 0
		} else {
			return 0
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def Long getSubTaskTotalForTypeInSeconds(Issue issue, SumType SumType) {
		Long subtaskTotal = 0

		Collection<Issue> subTaskIssues = issue.getSubTaskObjects()
		if (subTaskIssues != null && subTaskIssues.size() > 0) {
			for (Issue subTaskIssue : subTaskIssues) {
				subtaskTotal = subtaskTotal + getIssueValueForTypeInSeconds(subTaskIssue, SumType)
			}
		}

		return subtaskTotal
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def String getHTMLString(Long summedValue, Long epicValue, SumType SumType) {
		String orColour = ""
		String tooltipStr = ""
		String estimateStr = String.format("%.2f",summedValue / 3600)

		if (SumType == SumType.ORIGINAL) {
			if (epicValue) {
				Boolean alert = (summedValue > epicValue)
				orColour = alert ? mRED : mGREEN
				tooltipStr = getFormattedTooltipForOriginal(summedValue, epicValue)
			} else {
				orColour = mUNKNOWN
				tooltipStr = "This is a summed total - the Epic itself has no Original Estimate"
			}
		} else if (SumType == SumType.REMAINING) {
			Long actual = summedValue + getLoggedWorkInSeconds()
			Long estimate = getOriginalEstimateInSeconds()
			orColour = (actual > estimate) ? mAMBER : mGREEN
			tooltipStr = getFormattedTooltipForActual(actual, estimate)
		} else if (SumType == SumType.LOGGED) {
			Long actual = summedValue + getRemainingEstimateInSeconds()
			Long estimate = getOriginalEstimateInSeconds()
			orColour = (actual > estimate) ? mAMBER : mGREEN
			tooltipStr = getFormattedTooltipForActual(actual, estimate)
		} else {
			orColour = mUNKNOWN
			tooltipStr = ""
		}

		return "<p style=\"color:" +
               orColour +
               "\" title=\"" +
               tooltipStr +
               "\"><strong>" +
               estimateStr +
               "h</strong></p>"
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def String getFormattedTooltipForOriginal(Long summedValue, Long epicValue) {
		if (summedValue == epicValue) {
			return "After elaboration, this Epic is the same as the the Original Estimate"
		} else if (summedValue > epicValue) {
			Integer  increase = (summedValue - epicValue) / epicValue * 100
			return "After elaboration, this Epic is " + increase + "% bigger than the Original Estimate"
		} else {
			Integer  decrease = (epicValue - summedValue) / epicValue * 100
			return "After elaboration, this Epic is " + decrease + "% less than the Original Estimate"
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	def String getFormattedTooltipForActual(Long actual, Long estimate) {
		String differenceStr = String.format("%.2f",Math.abs(actual - estimate) / 3600)
		if (actual == estimate) {
			return "The ETC and logged work is the same as the summed estimate for this Epic"
		} else if (actual > estimate) {
			return "The ETC and logged work now exceed the summed estimate for this Epic by " + differenceStr + " hours"
		} else {
			return "The ETC and logged work is less than the summed estimate for this Epic by " + differenceStr + " hours"
		}
	}
}

