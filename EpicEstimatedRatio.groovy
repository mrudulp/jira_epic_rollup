import com.atlassian.jira.issue.Issue
import EpicEstimateUtils


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Issue issue = issue
def scriptEpicEstimateUtils = new EpicEstimateUtils(issue)
Long totalEpicEstimate = scriptEpicEstimateUtils.getOriginalEstimateInSeconds()
if (totalEpicEstimate) {
	if (issue.getOriginalEstimate()) {
		Double ratio = totalEpicEstimate / issue.getOriginalEstimate()
		return ratio
	} else {
		return null
	}
} else {
	return null
}
