# iData scanner pda web
 Desarrollo de escaneo de pda de la marca iData

En muchos escenarios comerciales, se involucrará el escaneo de códigos QR y PDA se enfoca en escanear dispositivos móviles. Las diferentes marcas de PDA tendrán diferentes métodos de llamada de escaneo. Hoy hablaremos sobre cómo obtener resultados de escaneo bajo la marca iData.

Los temas de hoy incluyen:

Cómo iData pda obtiene los resultados del escaneo
Introducción a la clase de paquete de escaneo IDataScan
El uso del escaneo en MainActivity
Código fuente de ScannerInterface
Representaciones y diagramas de estructura del proyecto.
Primero echemos un vistazo a las representaciones.

1. Cómo obtiene la pda iData los resultados del análisis
Después de ser compatible con múltiples PDA, existen aproximadamente dos formas de obtener los resultados del escaneo de PDA. Una es obtener la integración del código a través del paquete jar proporcionado por el fabricante y la otra es obtener los resultados del escaneo a través de la transmisión, iDta PDA. De lo que vamos a hablar hoy es de obtener resultados de escaneo a través de transmisión.

2. Introducción a la clase de paquete de escaneo IDataScan
IDataScan encapsula principalmente una clase de recepción de transmisión para recibir datos escaneados, que involucra principalmente varios métodos que deben usarse para llamadas externas:
