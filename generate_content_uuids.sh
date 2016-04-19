#!/bin/sh
username=$1
password=$2
rm -f src/test/resources/enrichedcontent/content.uuid.coco
curl -o /tmp/content.json "https://$username:$password@pre-prod-up.ft.com/__restorage-mongo/content/__ids"
cat /tmp/content.json | jq '.id' | sed 's/^.\(.*\).$/\1/' > src/test/resources/enrichedcontent/content.uuid.coco
sed -i '1iuuid' src/test/resources/enrichedcontent/content.uuid.coco