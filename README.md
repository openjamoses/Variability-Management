We identified and analyzed families of apps (Comprising of the main projects and its forks) that are maintained together and that exist both on the official app store (Google Play) as well as on Github to explores clone-based reuse practices for open- source Android apps: Publication Clone-Based Variability Management in the Android Ecosystem
This project uses GitHub Rest API to mine Project Details from Github Social Coding platform. 
To be able to Mine as many data using GitHub Rest API within a short period, You might need to generate many tokens, to avoid rate limit problem:

-	Search_for_Repos.java
The criteria below return the first 100 repos with at least 2 forks and are not fork example to search for repos with atleast 2 forks:
https://api.github.com/search/repositories?q=android%20app+forks:>=2+fork:false&sort=forks&order=asc&page=1per_page=100.  

-	FindMLP_GooglePlayApps.java – find out if the repos package name is on Google play. This Java file first identify the package name by reading the manifest file and use the package name to identify if it corresponds to the app on Google[play..

https://api.github.com/search/code?q=main+in:path+package+in:file+filename:AndroidManifest+repo:ProjectName>+extension:xml&page.

Note that: Android Manifest file is XML Format, and therefore we Used DOM to parse the file as bellow.
Document doc = null;
        try{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputStream inputStream = new    ByteArrayInputStream(xmlString.getBytes());
        doc =  dBuilder.parse(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return doc; 

-	Count_Atleast_6Commits.java , To eliminate all the repos with less than six (6) commits
By reading just the first page of the commits using the Github API, https://api.github.com/repos/<ProjectName>/commits
By just counting the size of the JSONARRAY Returned from the above URL We can know the total commits .

-	CollectMLP_FPPullrequestCommits.java To collect the pull request commits and the hanged lines of code, between Main line Projects and Its forks. 
https://api.github.com/repos/<ProjectName>/pulls/<Number>/commits
The all implementation is done inside the file: com.opm.variability.reads.PullrequestCommits.java

-	Collect_AppLanguage.java This file returns all the programming languages used on a given Repos name by reading the JSon Object returns from the REST API .

String language = (String) jSONObject.get("language");

-	https://api.github.com/repos/<ProjectName>.

-	Collect_GooglePlayStatistics.java, This file uses JSOUP to Parse the Google play html Pages corresponding to the Repos name: and return app details such as Number of downloads, Starts, Update date, Authors, Category, etc. as shown below:

To parse html page using JSOUP, code bellow is used::
Connection.Response res = Jsoup.connect("https://play.google.com/store/apps/details?id=<PackageName>hl=en).get()")
                                .method(Connection.Method.GET)
                                .execute();
              Document doc= res.parse();

- IdentifyForks_UniqueCommits.java : To identify the unique commits for the fork projects; 





