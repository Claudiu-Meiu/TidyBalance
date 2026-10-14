#!/bin/sh
set -eu

if [ "$(id -u)" -ne 0 ]; then
    echo "Error: run this script with sudo:"
    echo "  sudo ./install.sh"
    exit 1
fi

if [ -f .env ]; then
    echo ".env already exists. Nothing to do."
    exit 0
fi

if [ ! -f .env.prod.template ]; then
    echo "Error: .env.prod.template not found."
    exit 1
fi

echo "Generating MySQL production credentials..."

MYSQL_PASSWORD=$(
    docker run --rm alpine:3.22 \
    sh -c 'head -c 32 /dev/urandom | od -An -tx1 | tr -d " \n"'
)

MYSQL_ROOT_PASSWORD=$(
    docker run --rm alpine:3.22 \
    sh -c 'head -c 32 /dev/urandom | od -An -tx1 | tr -d " \n"'
)

cp .env.prod.template .env

sed -i "s/^MYSQL_PASSWORD=$/MYSQL_PASSWORD=$MYSQL_PASSWORD/" .env
sed -i "s/^MYSQL_ROOT_PASSWORD=$/MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD/" .env

chmod 644 .env

echo "Production environment initialized."
