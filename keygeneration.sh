openssl req -config qualitool.conf -new -x509 -sha256 -newkey rsa:2048 -nodes -keyout qualitool.key.pem -days 365 -out qualitool.cert.pem -subj "//CN=qualitool"
openssl pkcs12 -export -in qualitool.cert.pem -inkey qualitool.key.pem -out qualitool.p12 -passout pass:qualitool