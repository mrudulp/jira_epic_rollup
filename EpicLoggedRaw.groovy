import com.atlassian.jira.issue.Issue
import EpicEstimateUtils


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Issue issue = issue
def scriptEpicEstimateUtils = new EpicEstimateUtils(issue)
Long totalEpicLogged = scriptEpicEstimateUtils.getLoggedWorkInSeconds()
if (totalEpicLogged) {
	return totalEpicLogged as Double
} else {
	return null
}

