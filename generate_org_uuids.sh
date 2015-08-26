#!/bin/sh
rm -f src/test/resources/organisation/organisations.uuid
curl -o /tmp/organisations.json ftaps39395-law1b-eu-t/transformers/organisations
cat /tmp/organisations.json | jq '.[].apiUrl' | grep  -oh '[0-9a-f]\{8\}-[0-9a-f]\{4\}-[0-9a-f]\{4\}-[0-9a-f]\{4\}-[0-9a-f]\{12\}' > src/test/resources/organisation/organisations.uuid
rm /tmp/organisations.json
echo "Removing some data"
sed -i 'n; d' src/test/resources/organisation/organisations.uuid
sed -i '1d; n; d' src/test/resources/organisation/organisations.uuid
sed -i 'n; d' src/test/resources/organisation/organisations.uuid
sed -i '1d; n; d' src/test/resources/organisation/organisations.uuid
sed -i '1iuuid' src/test/resources/organisation/organisations.uuid
