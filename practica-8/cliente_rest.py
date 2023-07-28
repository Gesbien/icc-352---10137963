import requests

# URL base del servidor javalin-demo
BASE_URL = 'http://localhost:7000/api'  # Cambiar a la URL correcta si es necesario

def listar_estudiantes():
    url = f'{BASE_URL}/estudiante'
    response = requests.get(url)
    return response.json()

def consultar_estudiante(estudiante_id):
    url = f'{BASE_URL}/estudiante/{estudiante_id}'
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    elif response.status_code == 404:
        print(f"Estudiante con el ID {estudiante_id} no fue encontrado.")
        return None
    else:
        print(f"Error: {response.status_code} - {response.text}")
        return None

def crear_estudiante(matricula, nombre, carrera):
    url = f'{BASE_URL}/estudiante'
    data = {'matricula': matricula, 'nombre': nombre, 'carrera': carrera}
    response = requests.post(url, json=data)
    return response.json()

def borrar_estudiante(estudiante_id):
    url = f'{BASE_URL}/estudiante/{estudiante_id}'
    response = requests.delete(url)
    return response.json()

# Ejemplos de uso:
if __name__ == "__main__":
    # Listar Todos los estudiantes
    print("Listar Todos los estudiantes:")
    estudiantes = listar_estudiantes()
    print(estudiantes)

    # Crear un nuevo estudiante
    nuevo_estudiante = {
        "matricula": 20190416,
        "nombre": "Gesbien Rafael Nuñez Vargas",
        "carrera": "ISC"
    }
    print("\nCrear un nuevo estudiante:")
    nuevo_estudiante_creado = crear_estudiante(**nuevo_estudiante)
    print(nuevo_estudiante_creado)

    print("\nInsertando nuevo estudiante:")
    estudiantes = listar_estudiantes()
    print(estudiantes)

    # Consultar Estudiante
    estudiante_id = 20190416  # Cambiar el ID del estudiante que desees consultar
    print(f"\nConsultar Estudiante ID {estudiante_id}:")
    estudiante = consultar_estudiante(estudiante_id)
    print(estudiante)

    # Borrar un estudiante
    estudiante_id_a_borrar = 20190416  # Cambiar el ID del estudiante que desees borrar
    print("\nBorrar un estudiante:")
    resultado_borrado = borrar_estudiante(estudiante_id_a_borrar)
    print(resultado_borrado)

    print("\nActualizando Lista...")
    estudiantes = listar_estudiantes()
    print(estudiantes)
