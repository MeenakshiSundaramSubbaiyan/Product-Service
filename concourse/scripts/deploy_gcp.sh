#!/bin/bash
# Does a deployment to GCP

set -e -x  #exit on error, echo commands to terminal

gcloud auth activate-service-account --key-file=${GOOGLE_APPLICATION_CREDENTIALS}

ARTIFACT_ID="product-service"
GROUP_ID="com.myretail"
VERSION="0.0.1-SNAPSHOT"


#Artifactory treats dots (.) in the group name as forward slashes (/).
GROUP_ID_SLASHES=`echo ${GROUP_ID} | sed -e "s/\./\//g"`

JAR_NAME="${ARTIFACT_ID}-${VERSION}.jar"

URL_TO_JAR="https://maven.artifactory.myretail.com/artifactory/${ARTIFACTORY_REPO}/${GROUP_ID_SLASHES}/${ARTIFACT_ID}/${VERSION}/${JAR_NAME}"

#Fetch the JAR from Artifactory
wget $URL_TO_JAR

#Upload to deployment area in Google Cloud Storage.
gsutil cp ${JAR_NAME} "gs://${GCP_PROJECT}-artifacts/releases/${GROUP_ID}/${ARTIFACT_ID}/${VERSION}/"