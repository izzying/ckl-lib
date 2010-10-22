### Bash
Execute the following Bash script for installing BIRT in your local Maven repository

	#!/bin/sh
	BV=2.6 #Birt version
	PK=jar   #package type
	INSTALL_DIR=/tmp/BIRT
	BASEDIR=$INSTALL_DIR/birt_runtime_2_6_1/ReportEngine/lib
	BIRT_GROUP=org.eclipse.birt
	EMF_GROUP=org.eclipse.emf.ecore
	ACTUAL_DIR=`pwd`
	mkdir $BASEDIR
	cd $BASEDIR
	# Download current release 2.6.1
	wget http://www.eclipse.org/downloads/download.php?file=/birt/downloads/drops/R-R1-2_6_1-201009171723/birt-runtime-2_6_1.zip&url=http://mirror.selfnet.de/eclipse/birt/downloads/drops/R-R1-2_6_1-201009171723/birt-runtime-2_6_1.zip&mirror_id=1002 $BASEDIR
	unzip birt-runtime-2_6_1.zip
	cd $BASEDIR

	#We Assume thar the 3 emf jars have the same version number , and take org.eclipse.emf.common for reference
	# For 2.6 i changed the emf ecore xmi jar to have the same version as the common
	EMFV=`ls org.eclipse.emf.common*.jar | sed -e 's/\.jar$//' | sed -e 's/.*_//'`

	#BIRT APIs
	for i in `ls *api.jar | sed -e 's/\.jar$//'` ;
	do mvn install:install-file -DgroupId=$BIRT_GROUP -DartifactId=${i} -Dversion=$BV -Dpackaging=$PK -Dfile="$BASEDIR/${i}.$PK" -DgeneratePom=true ;
	done;

	#EMF ( Eclipse Modeling Framework ) . Assuming the 3 emf jars have the same version number!
	for i in `ls org.eclipse.emf.*.jar | sed -e 's/\.jar$//' | sed -e 's/_.*//'` ;
	do mvn install:install-file -DgroupId=${i} -DartifactId=${i} -Dversion=$EMFV -Dpackaging=$PK -Dfile="$BASEDIR/${i}_$EMFV.$PK" -DgeneratePom=true ;
	done;

	# Misc
	mvn install:install-file -DgroupId=org.w3c -DartifactId=flute -Dversion=1.3 -Dpackaging=jar -Dfile="flute.$PK" -DgeneratePom=true
	mvn install:install-file -DgroupId=org.w3c -DartifactId=sac -Dversion=1.3.0.v200805290154 -Dpackaging=jar -Dfile="org.w3c.css.sac_1.3.0.v200805290154.$PK" -DgeneratePom=true
	mvn install:install-file -DgroupId=com.ibm -DartifactId=icu -Dversion=4.2.1.v20100412 -Dpackaging=jar -Dfile="com.ibm.icu_4.2.1.v20100412.$PK" -DgeneratePom=true
	mvn install:install-file -DgroupId=com.lowagie -DartifactId=itext -Dversion=1.3 -Dpackaging=jar -Dfile="itext-1.3.$PK" -DgeneratePom=true
	mvn install:install-file -DgroupId=org.mozilla.rhino -DartifactId=js -Dversion=1.6R7 -Dpackaging=jar -Dfile="js.$PK" -DgeneratePom=true
	# Needed for IConnectionFactory
	mvn install:install-file -DgroupId=org.eclipse.birt -DartifactId=oda-jdbc -Dversion=2.6 -Dpackaging=jar -Dfile=`find ../plugins -name oda-jdbc.jar` -DgeneratePom=true	


