#!/bin/sh
rm -f src/test/resources/roles/roles.uuid
curl -o /tmp/roles.json ftaps50665-law1a-eu-t/transformers/roles
cat /tmp/roles.json | jq '.[].apiUrl' | grep  -oh '[0-9a-f]\{8\}-[0-9a-f]\{4\}-[0-9a-f]\{4\}-[0-9a-f]\{4\}-[0-9a-f]\{12\}' > src/test/resources/roles/roles.uuid
# rm /tmp/roles.json
echo "Removing some data"
sed -i 'n; d' src/test/resources/roles/roles.uuid
sed -i '1iuuid' src/test/resources/roles/roles.uuid
