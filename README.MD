# Anagram Finder
## cURL request example
```json
curl --location 'localhost:8080/anagram' \
--header 'Content-Type: application/json' \
--data '{
    "target": "angel",
    "source": "glean",
    "checkRealWord": "true"
}'
```