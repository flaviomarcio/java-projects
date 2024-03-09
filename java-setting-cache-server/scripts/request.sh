curl -i -X POST -H 'Content-Type: application/json' http://localhost:8080/api/settings/write -d '{"name":"test-1", "settings":["env001=test"]}'
curl -i -X POST -H 'Content-Type: application/json' http://localhost:8080/api/settings/write -d '{"name":"test-2", "settings":{"env001":"test"}}'
curl -i -X POST -H 'Content-Type: application/json' http://localhost:8080/api/settings/write -d '{"name":"test-2", "settings":"1901-01-01T00:00:00.000"}'
curl -i -X GET -H 'Content-Type: application/json' http://localhost:8080/api/settings/exists?name=test-1
curl -i -X GET -H 'Content-Type: application/json' http://localhost:8080/api/settings/read?name=test-1
curl -i -X GET -H 'Content-Type: application/json' http://localhost:8080/api/settings/setting?name=test-1

curl -i -X POST -H 'Content-Type: application/json' http://development-company-srv-setting:80/api/settings/write -d '{"name":"test-1", "settings":["env001=test"]}'
curl -i -X POST -H 'Content-Type: application/json' http://development-company-srv-setting:80/api/settings/write -d '{"name":"test-2", "settings":{"env001":"test"}}'
curl -i -X POST -H 'Content-Type: application/json' http://development-company-srv-setting:80/api/settings/write -d '{"name":"test-2", "settings":"1901-01-01T00:00:00.000"}'
curl -i -X POST -H 'Content-Type: application/json' http://development-company-srv-setting:80/api/settings/write -d ''
curl -i -X GET -H 'Content-Type: application/json' http://development-company-srv-setting:80/api/settings/exists?name=test-1
curl -i -X GET -H 'Content-Type: application/json' http://development-company-srv-setting:80/api/settings/read?name=test-1
curl -i -X GET -H 'Content-Type: application/json' http://development-company-srv-setting:80/api/settings/setting?name=test-1
