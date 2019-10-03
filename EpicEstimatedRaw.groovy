import com.atlassian.jira.issue.Issue
import EpicEstimateUtils


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Issue issue = issue
def scriptEpicEstimateUtils = new EpicEstimateUtils(issue)
Long totalEpicEstimate = scriptEpicEstimateUtils.getOriginalEstimateInSeconds()
if (totalEpicEstimate) {
	return totalEpicEstimate as Double
} else {
	return null
}

