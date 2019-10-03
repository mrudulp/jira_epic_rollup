import com.atlassian.jira.issue.Issue
import EpicEstimateUtils


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Issue issue = issue
def scriptEpicEstimateUtils = new EpicEstimateUtils(issue)
Long totalEpicRemaining = scriptEpicEstimateUtils.getRemainingEstimateInSeconds()
if (totalEpicRemaining) {
	return totalEpicRemaining as Double
} else {
	return null
}

