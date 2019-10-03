import com.atlassian.jira.issue.Issue
import EpicEstimateUtils
import SumType


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Issue issue = issue
def scriptEpicEstimateUtils = new EpicEstimateUtils(issue)
Long totalEpicRemaining = scriptEpicEstimateUtils.getRemainingEstimateInSeconds()
if (totalEpicRemaining) {
	return scriptEpicEstimateUtils.getHTMLString(totalEpicRemaining, issue.getEstimate(), SumType.REMAINING)
} else {
	return null
}

