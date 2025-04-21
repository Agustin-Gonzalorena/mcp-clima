#!/bin/bash
# Obtener la ruta del directorio donde está el script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Ir al directorio del script
cd "$SCRIPT_DIR"

# Ejecutar el comando Maven
mvn exec:java -Dexec.mainClass=org.example.Main
