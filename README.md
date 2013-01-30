tolpen
======
Tolpen is a stress testing tool for a Full Text Search Cluster

Install
-------
sudo apt-get install maven git
git clone git://github.com/indexisto/tolpen.git
cd tolpen
ln -s ~/apache-jmeter-2.8/ jmeter

Configure
---------
There is no properties file. To configure tool, please modify com.indexisto.tool.tolpen.config.Config.. and rebuid the project

Execute
-------
- Step1: prepare documents repo
<p>mvn clean package exec:java -Dexec.args="prune"

- Step2: prepare indecies and requests repos
<p>mvn clean package exec:java -Dexec.args="prepare"

- Step3: launch jmeter
<p>mvn chronos-jmeter:jmeter

- Step4: get results
<p>mvn chronos-jmeter:jmeter
