import zeep

wsdl = 'http://localhost:7000/ws/EstudianteWebServices?wsdl'
client = zeep.Client(wsdl=wsdl)
factory = client.type_factory('ns0')

print("Lista estudiantes:")
print(client.service.getListaEstudiante())

print("\n\nInsertando estudiante...")
est = factory.estudiante(matricula=20190315, nombre='Abraham Breton', carrera = 'ISC')
client.service.crearEstudiante(arg0 = est)

print("\n\nLista actualizada:")
print(client.service.getListaEstudiante())

print("\n\nConsultando estudiante 20011136:")
print(client.service.getEstudiante(20011136))

print("\n\nActualizando estudiante 20190315...")
est = factory.estudiante(matricula=20190315, nombre='Abraham Breton', carrera = 'ICC')
client.service.actualizarEstudiante(arg0 = est)

print("\n\nLista actualizada:")
print(client.service.getListaEstudiante())

print("\n\nEliminando estudiante...")
print(client.service.eliminandoEstudiante(20190315))

print("\n\nLista actualizada:")
print(client.service.getListaEstudiante())