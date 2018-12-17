Code and data for our study "Clone-Based Variability Management in the Android Ecosystem" presented the 34th IEEE International Conference on Software Maintenance and Evolution (ICSME), 2018.


Clone-Based Variability Management in the Android Ecosystem

Abstract

"Mobile app developers often need to create variants to account for different customer segments, payment models or functionalities. A common strategy is to clone (or fork) an existing app and then adapt it to new requirements. This form of reuse has been enhanced with the advent of social-coding platforms such as GitHub, cultivating a more systematic reuse. Different facilities, such as forks, pull requests, and cross-project traceability support clone-based development. Unfortunately, even though, many apps are known to be maintained in many variants, little is known about how practitioners manage variants of mobile apps.
We present a study that explores clone-based reuse practices for open-source Android apps. We identified and analyzed families of apps that are maintained together and that exist both on the official app store (Google Play) as well as on GitHub, allowing us to analyze reuse practices in depth. We mined both repositories to identify app families and to study their characteristics, including their variabilities as well as code-propagation practices and maintainer relationships. We found that, indeed, app families exist and that forked app variants fall into the following categories: (i) re-branding and simple customizations, (ii) feature extension, (iii) supporting of the mainline app, and (iv) implementation of different, but related features. Other notable characteristic of the app families we discovered include: (i) 73% of the app families did not perform any form of code propagation, and (ii) 74% of the app families we studied do not have common maintainers."

Requirements
poi-3.14.jar or higher

• poi-ooxml-3.14.jar or higher

• poi-ooxml-schemas-3.14.jar or higher

• xmlbeans-2.6.0.jar or higher

• json-simple-1.1.1.jar or higher

• jsoup-1.11.3.jar or higher

Data Mining Scripts
•	Search_for_Repos.java – searching repositories on GitHub
•	FindMLP_GooglePlayApps.java – Checks if the mainline variants exists on Google Play store
•	Count_Atleast_6Commits.java - Eliminates all the repos with less than six (6) commits on GitHub
•	CollectMLP_FPPullrequestCommits.java – Collects the merged commits of the pull requests sent from the mainline to the fork variant or fork variant to mainline variant.
•	Collect_AppLanguage.java – Collects Apps the programming languages.
•	Collect_GooglePlayStatistics.java - Collects Apps Google Play meta-data
•	IdentifyForks_UniqueCommits.java : Collects unique commits for the fork variants

Data Analysis Scripts






