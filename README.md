# AlimentadorPerros
Proyecto de cursada de la materia Sistemas Operativos Avanzados.

# Presentación

## Materia: Sistemas Operativos Avanzados

## Alumnos: 
* Brude, Alejandro; DNI 33908097
* Castillo, Tomás Eugenio; DNI 39769558
* Fernandez, Julian Gonzalo; DNI 38457070
* Fernandez, Nicolas; DNI 38168581
* Orlando, Javier; DNI 34850430
* Vargas, Gabriel; DNI 38059006

## Nombre del grupo: M1

## Nombre del proyecto: Alimentador Perros

Este proyecto consta de un alimentador automático de perros, linkeado con una aplicación Android que configure el alimentador.

# Descripción del proyecto:
Este proyecto consta de un alimentador automático de perros, linkeado con una aplicación Android que configure el alimentador. El usuario podrá configurar horarios de comida con la app, así el alimentador soltará la comida a esa hora o bien manualmente el usuario podrá decidir el momento de alimentar a su perro manualmente con un botón en la app. 


## ¿Qué es el proyecto?

Este proyecto consta de un alimentador automático de perros


## ¿Qué hace? 

Entre sus funcionalidades, encontramos:
* Gestión de la rapidez de la entrega de comida: el alimentador podrá soltar de a muchos o a pocos granos de comida, según configure el usuario en la app.
* Gestión de la dosis de la comida: el usuario podrá elegir la cantidad de comida para cada tipo de perro.
* Gestión de horarios de alimentación: el usuario podrá elegir los horarios de cada día para alimentar a su perro.
* Alimentar manualmente: el usuario con un movimiento del celular detectado por los sensores de Android,  la app podrá alimentar a su perro en el momento.
* Determinación de restos: el sistema recolectará datos sobre si el perro dejó comida o no, cada vez que comió.
* Gestión de perfil del perro: el usuario podrá cargar al sistema el nombre, edad, raza, peso y altura de su perro.
* Sugerencia de rutina especial para el perro: en caso de enfermedad del perro o la necesidad de que el mismo suba o baje de peso, la app le sugerirá rutinas al usuario que puede seguir.
* Sugerencia de rutina para el perro en base a su perfil (raza, edad, tamaño)
* El sistema produce un alerta de recarga de comida en el compartimiento de comida y notifica a la app. 
* Estadísticas de comidas
* Alertas: el sistema notificara alertas en la app,  si el perro comió mucho o poco en el día.
* Acondicionamiento canino: El sistema emitira un sonido si detecta que el perro come muy rapido , para acondicionar la conducta alimenticia del mismo.
       


## ¿Qué problema soluciona? 

El sistema podrá alimentar automáticamente al perro, con los horarios configurados por el usuario, además podrá sugerir rutinas en caso de que el perro tenga problemas de enfermedad o de peso.


## ¿Cómo se usa?

El usuario podrá configurar horarios de comida con la app, así el alimentador soltará la comida a esa hora o bien manualmente el usuario podrá decidir el momento de alimentar a su perro manualmente con un botón en la app. 


# Objetivos del sistema

* Cumplir con los horarios de comida configurados por el usuario
* Cumplir con las dosis de comida configuradas por el usuario
* Cumplir con las dosis de comida segun el comportamiento del perro.


## ¿Como lo hace?

El usuario deberá cargar comida en un depósito. En la app, el usuario debe cargar el perfil de su perro 

## ¿Qué ofrece como resultado?


# Descripción Técnica de todo el sistema:
## Actuadores: 
* Servo sg90. Expide la comida del alimentador hacia el plato, va variando el ángulo de apertura del depósito para expedir más o menos comida.
* Buzzer activo: El sistema emitira un sonido si detecta que el perro come muy rapido , para acondicionar la conducta alimenticia del mismo.
* Luces LED: se activará una luz led para indicar la falta de comida en el depósito para que el usuario haga una recarga de comida.

## Sensores
* Sensor de peso(celda de carga). Detecta la carga de comida dentro del plato de la mascota, y cuánto comió el perro.
* Sensor de ultrasonido. Detecta el nivel de alimento en el depósito.
* Sensor PIR. Detecta la presencia de la mascota cuando la misma se encuentra a X cm de distancia (o cercana) del plato de comida.

## Reloj RTC. Permite programar los eventos para la descarga de comida y alertas acerca del comportamiento de la mascota.


Que lógica tiene el sistema completo. Como es el
sistema completo, Procedimientos, procesos, insumos, resultados, servicios en la nube.


# Descripción de partes:

# Diagramas

## De bloques, partes

## Funcional

![Funcional](https://github.com/tomascastillo/M1/blob/master/Sistema-Embebido/Diagramas/Diagrama%20funcional.jpg)

## Físico (bloques y boceto)

![Fisico](https://github.com/tomascastillo/M1/blob/master/Sistema-Embebido/Diagramas/Diagrama%20fisico.jpg)

## Software (nombre real/final de las funciones)

## Lógico

# Firmware
: Como es el firmware, funciones realizadas, estados, interacciones
- Descripción de la Aplicación Android:

## Descripción de la Aplicación móvil. ¿Cuál es su funcionalidad?

## Explicación del funcionamiento

## Activitys que la conforman, explicación de las partes. Menú de secuencias de
Activitys, árbol de opciones. (captura de pantalla).

## Descripción de los sensores utilizados del Smartphone

## Diagrama de comunicación con el sistema embebido. Metodología utilizada para
comunicarse (Bluetooth, Wifi, Servicios web, Servidor en la nube, Api, REST,
Firebase, etc.)

## Herramientas complementarias utilizadas: Si se usa base de datos (SQLite, etc.)

# Manual de uso:

# Conclusiones

## Qué libertades se tomaron el en prototipado y por qué (motivo)

## Que seguirán desarrollando

## Cómo lo mejorarían con mínima inversión (lógica, soft)
