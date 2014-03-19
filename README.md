Model of using built-in apps in spring-yarn-boot follows logic:

```
10:29 $ java -jar build/libs/gs-yarn-appmodel-client-0.1.0.jar --operation INSTALL
New instance app installed

10:30 $ java -jar build/libs/gs-yarn-appmodel-client-0.1.0.jar --operation LISTINSTALLED
  NAME  PATH
  ----  -------------------------
  app   hdfs://localhost:8020/app

10:30 $ java -jar build/libs/gs-yarn-appmodel-client-0.1.0.jar --operation SUBMIT
New instance submitted with id application_1395058039949_0052

10:31 $ java -jar build/libs/gs-yarn-appmodel-client-0.1.0.jar --operation LISTSUBMITTED -v
  APPLICATION ID                  USER          NAME              QUEUE    TYPE  STARTTIME       FINISHTIME      STATE     FINALSTATUS  ORIGINAL TRACKING URL
  ------------------------------  ------------  ----------------  -------  ----  --------------  --------------  --------  -----------  ---------------------
  application_1395058039949_0052  jvalkealahti  gs-yarn-appmodel  default  GS    19/03/14 10:31  N/A             RUNNING   UNDEFINED    N/A


10:31 $ java -jar build/libs/gs-yarn-appmodel-client-0.1.0.jar --operation KILL -a application_1395058039949_0052
Kill request for application_1395058039949_0052 sent


10:31 $ java -jar build/libs/gs-yarn-appmodel-client-0.1.0.jar --operation LISTSUBMITTED -v
  APPLICATION ID                  USER          NAME              QUEUE    TYPE  STARTTIME       FINISHTIME      STATE     FINALSTATUS  ORIGINAL TRACKING URL
  ------------------------------  ------------  ----------------  -------  ----  --------------  --------------  --------  -----------  ---------------------
  application_1395058039949_0052  jvalkealahti  gs-yarn-appmodel  default  GS    19/03/14 10:31  19/03/14 10:31  KILLED    KILLED

```

