import com.atlassian.jira.issue.Issue
import EpicEstimateUtils
import SumType


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Issue issue = issue
def scriptEpicEstimateUtils = new EpicEstimateUtils(issue)
Long totalEpicEstimate = scriptEpicEstimateUtils.getOriginalEstimateInSeconds()
if (totalEpicEstimate) {
	return scriptEpicEstimateUtils.getHTMLString(totalEpicEstimate, issue.getOriginalEstimate(), SumType.ORIGINAL)
} else {
	return null
}
