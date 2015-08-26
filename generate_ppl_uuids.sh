#!/bin/sh
rm -f src/test/resources/people/people.uuid
curl -o /tmp/people.json ftaps35629-law1a-eu-t/transformers/people
cat /tmp/people.json | jq '.[].apiUrl' | grep  -oh '[0-9a-f]\{8\}-[0-9a-f]\{4\}-[0-9a-f]\{4\}-[0-9a-f]\{4\}-[0-9a-f]\{12\}' > src/test/resources/people/people.uuid
# rm /tmp/people.json
echo "Removing some data"
sed -i 'n; d' src/test/resources/people/people.uuid
sed -i '1d; n; d' src/test/resources/people/people.uuid
sed -i 'n; d' src/test/resources/people/people.uuid
sed -i '1d; n; d' src/test/resources/people/people.uuid
sed -i '1iuuid' src/test/resources/people/people.uuid
