package com.dealerpilothr.logic;

public class Environment {
    public static final String ID = "id";
    public static final String KEY = "passkey";
    public static final String PARAM_TASK = "task";
    public static final String BROADCAST_ACTION = "action";

    public static final String SERVER = "https://apiv1.dealerpilothr.com";
    public static final String SERVERFull = "https://portal.dealerpilothr.com";
    public static final String GoogleURLReadPDF = "https://docs.google.com/viewer?url=";
    public static final String DocumentManagementFilesDownloadURL = "/v1/DocumentManagement/Files/Download?fileId=";
    public static final String SecurityAuthenticationsURL = "/v1/Security/Authentications";
    public static final String PoliciesCurrentURL = "/v1/Policies/Current";
    public static final String PoliciesLogURL = "/v1/Policy/Log";
    public static final String CoursesCurrentURL = "/v1/CoursesFiles/GetCurrent";
    public static final String CourseFileURL = "/api/CourseFile";
    public static final String QuizzesURL = "/v1/Quizzes";
    public static final String QuizzesLogURL = "/v1/Quizzes/Log";
    public static final String WorkplaceInspectionTemplatesURL = "/v1/HealthAndSafety/WorkplaceInspectionTemplates";
    public static final String WorkplaceInspectionTemplateItemsURL = "/v1/HealthAndSafety/WorkplaceInspectionTemplateItems";
    public static final String ProfilesURL = "/v1/Profiles";
    public static final String CourseLogURL = "/v1/Courses/Files/Logs";
    public static final String ContactsURL = "/v1/Contacts";
    public static final String WorkplaceInspectionsCurrentURL = "/v1/HealthAndSafety/WorkplaceInspections/Current";
    public static final String OrganizationStructureURL = "/v1/Security/OrganizationStructures";
    public static final String OrganizationsOrganizationStructureURL = "/v1/Security/Organizations/OrganizationStructures";
    public static final String WorkplaceInspectionItemsURL = "/v1/HealthAndSafety/WorkplaceInspections/Items";
    public static final String WorkplaceInspectionsURL = "/v1/HealthAndSafety/WorkplaceInspections";
    public static final String WorkplaceInspectionsItemPrioritiesURL = "/v1/HealthAndSafety/WorkplaceInspections/ItemPriorities";
    public static final String WorkplaceInspectionsItemStatusesURL = "/v1/HealthAndSafety/WorkplaceInspections/ItemStatuses";
    public static final String InvestigationsURL = "/v1/HealthAndSafety/Investigations";
    public static final String DocumentManagementFilesURL = "/v1/DocumentManagement/Files";
    public static final String DocumentManagementFilesUploadURL = "/v1/DocumentManagement/Files/OrganizationSystemFolder";
    public static final String WorkplaceInspectionsFilesURL = "/v1/HealthAndSafety/WorkplaceInspections/Files";
    public static final String WorkplaceInspectionsItemsFilesURL = "/v1/HealthAndSafety/WorkplaceInspections/Items/Files";
    public static final String InvestigationsCurrentURL = "/v1/HealthAndSafety/Investigations";
    public static final String InvestigationsFilesURL = "/v1/HealthAndSafety/Investigations/InvestigationFiles";
    public static final String InvestigationItemsURL = "/v1/HealthAndSafety/Investigations/Items";
    public static final String InvestigationsItemsFilesURL = "/v1/HealthAndSafety/Investigations/Items/InvestigationItemFiles";
    public static final String TopicTemplatesURL = "/v1/HealthAndSafety/TopicTemplates";
    public static final String TopicTemplateItemsURL = "/v1/HealthAndSafety/TopicTemplates/Items";
    public static final String MembersURL = "/v1/HealthAndSafety/Members";
    public static final String MeetingsURL = "/v1/HealthAndSafety/Meetings";
    public static final String MeetingsFilesURL = "/v1/HealthAndSafety/Meetings/Files";
    public static final String PerformanceEvaluationsURL = "/v1/PerformanceEvaluations";
    public static final String PerformanceEvaluationsFilesURL = "/v1/PerformanceEvaluations/AttachFile";
    public static final String DisciplinesURL = "/v1/Disciplines";
    public static final String DisciplinesFilesURL = "/v1/Disciplines/AttachFile";
    public static final String DocumentsURL = "/v1/Docs";
    public static final String DocumentsFilesURL = "/v1/Docs/AttachFile";
    public static final String DocCategoriesURL = "/v1/Docs/Categories";
    public static final String TimeOffRequestsURL = "/v1/WorkforceManagement/TimeoffRequests";
    public static final String TimeBanksURL = "/v1/WorkforceManagement/TimeBankAccounts";
    public static final String AttendanceCodesURL = "/v1/WorkforceManagement/AttendanceCodes";
    public static final String AddAvatarURL = "/v1/Images/AddAvatar";
    public static final String DeleteAvatarURL = "/v1/Images/DeleteAvatar";
    public static final String TaskManagementLinksURL = "/v1/TaskManagement/links";
    public static final String TaskManagementParametersURL = "/v1/TaskManagement/Parameters";
    public static final String TasksURL = "/v1/TaskManagement/Tasks";
    public static final String AssignURL = "/v1/TaskManagement/Assign";
    public static final String PunchesCategoriesURL = "/v1/Punches/Categories";
    public static final String PunchesURL = "/v1/Punches/AddProfilePunches";
    public static final String AttendancesURL = "/v1/WorkforceManagement/Attendances";
    public static final String BulletinURL = "/v1/News";
    public static final String AccidentsURL = "/v1/Accidents";
    public static final String AddressesURL = "/v1/Addresses";
    public static final String HealthAndSafetyMemberURL = "/v1/Profiles/HealthAndSafetyMember";
    public static final String ProfilesEmployeeInformationURL = "/v1/Profiles/EmployeeInformation";
    public static final String ProfilesContactInformationURL = "/v1/Profiles/ContactInformation";
    public static final String ProfilesPersonalAdditionalInformationURL = "/v1/Profiles/PersonalAdditionalInformation";
    public static final String ProfilesAdditionalInformationURL = "/v1/Profiles/WorkAdditinalInformation";
    public static final String ProfilesCustomFieldsURL = "/v1/Profiles/CustomFields";
    public static final String SafetisURL = "/v1/HealthAndSafety/SafetyDataSheets";
    public static final String SafetisCurrentURL = "/v1/HealthAndSafety/SafetyDataSheets";
    public static final String SafetisFilesURL = "/v1/HealthAndSafety/SafetyDataSheets/AttachFile";
    public static final String CurrentOrganizationStructuresURL = "/v1/Security/Organizations/CurrentOrganizationStructures";
    public static final String EventsURL = "/v1/Calendar/Events";
    public static final String MeetingsReportURL = "/v1/HealthAndSafety/Meetings/Report?id=";
    public static final String WorkplaceInspectionsReportURL = "/v1/HealthAndSafety/WorkplaceInspections/Report?id=";
    public static final String QuizzesReportURL = "/v1/Quizzes/Log/Report?id=";

    public static final String PolicyStatusStarted = "1586dc39-979b-3991-c822-a4bcb14d1345";
    public static final String PolicyStatusAcknowledged = "1586dc39-979b-ab1c-838f-1917fce340de";
    public static final String AccountQuizStatusStarted = "af29dc39-68b9-d431-2653-724eb8a104e8";
    public static final String AccountCourseFileStatus = "c9c8db39-2b01-0997-33a6-699e5ed75383";
    public static final String AccountCourseFileStatusFinish = "c9c8db39-2b01-b87e-3632-1589834c115e";
    public static final String EmergencyContactType = "c9c8db39-2b01-dc72-4427-c51834bbec82";
    public static final String FamilyContactType = "c9c8db39-2b01-c6c9-27ff-eb9e0a5b0cf0";
    public static final String StatusFail = "dadadc39-af79-54ae-7c6f-44b1b8be7d38";
    public static final String TaskTypeItemId = "14eddc39-49f6-16c5-ecb5-550eb6e2083a";
    public static final String TaskTypeWorkplaceId = "c5b2dd39-03b1-c9b8-fb19-feaf9c6e0d36";
    public static final String TaskTypeAddItemId = "14eddc39-49f6-2186-f336-2a6a5de14644";
    public static final String TaskTypeAddWorkplaceId = "14eddc39-49f6-3e64-08b6-5daab5b7268e";
    public static final String CategoryAddId = "14eddc39-49f6-3b9e-f38a-fef408856a6b";
    public static final String TimeOffRequestAddId = "7512de39-ee8b-007c-8da3-05343f1a6bcb";

    public static final String IcoEdit = "61504";
    public static final String IcoDelete = "61944";
    public static final String IcoFiles = "61678";
    public static final String IcoAssign = "61452";
    public static final String IcoReport = "61889";
    public static final String IcoOk = "61452";
    public static final String IcoClose = "61453";
    public static final String IcoAdd = "61543";
    public static final String IcoOpen = "61889";
    public static final String IcoPlay = "61515";
    public static final String IcoList = "61498";
    public static final String IcoDownload = "61465";
    public static final String IcoAddTask = "62065";
    public static final String IcoTask = "61747";
    public static final String IcoLock = "61758";
    public static final String IcoAttach = "61638";
}
