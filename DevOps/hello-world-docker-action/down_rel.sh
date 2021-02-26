#!/bin/bash
# set -x
set -e
if [ $# -lt 5 ] ;then
    echo "Usage: <GitHub token> <Org/Repo> <Release> <Filename> <tar/zip>"
    exit 1
fi
TOKEN="$1"
REPO="$2"
VERSION=$3
FILE=$4
COMPRESSION_TYPE=$5
GITHUB_API_ENDPOINT="api.github.com"
function github_curl() {
  curl -sL -H "Authorization: token $TOKEN" \
       -H "Accept: application/vnd.github.v3+json" \
       $@
}
RELEASE_RESPONSE=`github_curl https://$GITHUB_API_ENDPOINT/repos/$REPO/releases`
echo ${RELEASE_RESPONSE} | jq
ASSET_ID=`echo ${RELEASE_RESPONSE} | jq -c ".[] | select(.name == \"${VERSION}\").id"`
echo "ASSET_ID: ${ASSET_ID}"
if [ "$ASSET_ID" = "null" ]; then
  echo "ERROR: version not found $VERSION"
  exit 1
fi
# github_curl https://$GITHUB_API_ENDPOINT/repos/$REPO/releases/${ASSET_ID}/assets
# curl -sL --header "Authorization: token $TOKEN" --header 'Accept: application/octet-stream' \
#      https://$TOKEN:@$GITHUB_API_ENDPOINT/repos/$REPO/releases/assets/$ASSET_ID
DOWNLOAD_URL=
if [ "$COMPRESSION_TYPE" = "zip" ]; then
    zipball_url=`echo ${RELEASE_RESPONSE} | jq -c ".[] | select(.name == \"${VERSION}\").zipball_url"`
    echo "zipball_url: ${zipball_url}"
    if [ "$zipball_url" != "" ]; then
        DOWNLOAD_URL=https://$TOKEN:@$GITHUB_API_ENDPOINT/repos/$REPO/zipball/${VERSION}
        FILE=${FILE}.zip
    fi
fi
if [ "$COMPRESSION_TYPE" = "tar" ]; then
    tarball_url=`echo ${RELEASE_RESPONSE} | jq -c ".[] | select(.name == \"${VERSION}\").tarball_url"`
    echo "tarball_url: ${tarball_url}"
    if [ "$tarball_url" != "" ]; then
        DOWNLOAD_URL=https://$TOKEN:@$GITHUB_API_ENDPOINT/repos/$REPO/tarball/${VERSION}
        FILE=${FILE}.tar.gz
    fi
fi
if [ "$DOWNLOAD_URL" != "" ]; then
    curl -sL --header "Authorization: token $TOKEN" --header 'Accept: application/json' ${DOWNLOAD_URL} > $FILE
    echo "Downloaded: ${FILE}"
    file ${FILE}
else
    echo "Could not locale the release"
fi
