import com.atlassian.jira.issue.Issue
import EpicEstimateUtils
import SumType


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Issue issue = issue
def scriptEpicEstimateUtils = new EpicEstimateUtils(issue)
Long totalEpicLogged = scriptEpicEstimateUtils.getLoggedWorkInSeconds()
if (totalEpicLogged) {
	return scriptEpicEstimateUtils.getHTMLString(totalEpicLogged, issue.getTimeSpent(), SumType.LOGGED)
} else {
	return null
}

