# Get OpenAPI cli

[cli 7.12.0] (https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/7.12.0/openapi-generator-cli-7.12.0.jar)

# Generate DEBUG
cd .\app\src\main\resources
java.exe -jar openapi-generator-cli-7.12.0.jar generate -g go -o out -i test_1.yml --global-property debugModels=true