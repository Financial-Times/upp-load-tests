#!/bin/sh
rm -f src/test/resources/memberships/memberships.uuid
curl -o /tmp/memberships.json ftaps50665-law1a-eu-t/transformers/memberships
cat /tmp/memberships.json | jq '.[].apiUrl' | grep -oh '[0-9a-f]\{8\}-[0-9a-f]\{4\}-[0-9a-f]\{4\}-[0-9a-f]\{4\}-[0-9a-f]\{12\}' > src/test/resources/memberships/memberships.uuid
# rm /tmp/memberships.json
echo "Removing some data"
sed -i 'n; d' src/test/resources/memberships/memberships.uuid
sed -i '1d; n; d' src/test/resources/memberships/memberships.uuid
sed -i 'n; d' src/test/resources/memberships/memberships.uuid
sed -i '1d; n; d' src/test/resources/memberships/memberships.uuid
sed -i '1iuuid' src/test/resources/memberships/memberships.uuid