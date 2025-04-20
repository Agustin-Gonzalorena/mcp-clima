#!/bin/bash

# Obtiene la ruta del directorio donde está este script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Cambia al directorio del proyecto (el mismo donde está el script)
cd "$SCRIPT_DIR" || exit 1

# Ejecuta el comando Maven
mvn exec:java -Dexec.mainClass=org.example.Main
