#!/bin/bash

cat << EOF > run.sh
#!/bin/bash
`grep "=" .env | tr '\n' ' '`mvn spring-boot:run
EOF

bash run.sh