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

Este proyecto consta de un alimentador automático de perros, linkeado con una aplicación Android que configura el alimentador.

# Descripción del proyecto:
Este proyecto consta de un alimentador automático de perros, linkeado con una aplicación Android que configura el alimentador. El usuario deberá solamente configurar el perfil del perro con la app, de esta forma, el sistema programará y mostrará la rutina que deberá seguir el can.  


## ¿Qué es el proyecto?

Este proyecto consta de un alimentador automático de perros


## ¿Qué hace?

* Alimentar manualmente: el  usuario con un movimiento del celular detectado por los sensores de Android,  la app podrá alimentar a su perro en el momento.
* Determinación de restos: el sistema recolectará datos sobre si el perro dejó comida o no, cada vez que comió.
* Gestión de perfil del perro: el usuario podrá cargar al sistema la raza, peso, fecha de nacimiento, nivel de actividad y estado del perro.
* El sistema configura la rutina para el perro en base a su perfil cargado o por defecto.
* La app muestra el historial de comidas .
* La app muestra notificaciones al usuario con respecto a la dieta del perro o el funcionamiento del alimentador.
* Acondicionamiento canino: El sistema emitira un sonido si detecta que el perro come muy rapido , para acondicionar la conducta alimenticia del mismo.



## ¿Qué problema soluciona?

El sistema podrá alimentar automáticamente al perro, sin intervencion del usuario.


## ¿Cómo se usa?

EL usuario ingresa a la app y elige el tipo de perfil que desea utilizar.
Si el usuario elige Crear perfil , el sistema le mostrara el formulario para cargar los datos del perro.
Luego el sistema configura la rutina , habilita el uso del alimentador y muestra el menu principal.
Si el usuario no elige crear perfil , el sistema genera un perfil y rutina por defecto, habilita el uso del alimentador y muestra el menu principal.
Luego , el usuario puede visualizar desde el menu principal las siguientes opciones
- Modificar Perfil
- Mostrar Historial de comidas
- Mostrar historial de notificaciones
- Recarga historial de comidas.
Ademas en la misma pantalla el usuario puede visualizar la rutina programada por el sistema.


# Objetivos del sistema

* Cumplir con las dosis de comida segun el comportamiento del perro.
* Notificar al usuario del comportamiento del perro.


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

## Funcional

![Funcional](https://github.com/tomascastillo/M1/blob/master/Sistema-Embebido/Diagramas/Diagrama%20Funcional.png)

## Físico (bloques y boceto)

![Fisico](https://github.com/tomascastillo/M1/blob/master/Sistema-Embebido/Diagramas/esquema%20fisico.png)

## Software (nombre real/final de las funciones)
![Software](https://github.com/tomascastillo/M1/blob/master/Sistema-Embebido/Diagramas/Diagrama%20de%20Software.png)

## Lógico
![Logico](https://github.com/tomascastillo/M1/blob/master/Sistema-Embebido/Diagramas/Diagrama_logico1.png)

## Conexión
![Conexión](https://github.com/tomascastillo/M1/blob/master/Sistema-Embebido/Diagramas/diagramaCircuito.jpg)


## Manual de uso

Al iniciar la aplicación, el sistema nos preguntará si queremos crear un perfil o cargar un perfil por defecto.

### Crear Perfil

Si se selecciona la opción de crear un perfil, se deberá ingresar peso, edad (en meses), si el perro está excedido de peso, con sobrepeso, excedido, o flaco, y su nivel de actividad (bajo, medio, alto). Luego se mostrará una rutina recomendada.
Si no se desea usar la rutina recomendada, se podrá crear una rutina personalizada.


### Cargar perfil por defecto

Si se selecciona esta opción, el usuario tendrá un perfil de perro por defecto y se recomendará una rutina por defecto.

### Menú principal

En el menú principal tendremos las siguientes opciones:


#### Modificar perfil

En esta opción, se podrán cambiar los datos del perfil del perro.

#### Recarga manual del plato

En esta opción, el usuario solo con mover el celular  podrá alimentar al perro en el momento.

#### Mostrar historial de comidas

En esta opción, se mostrará el historial con fecha y hora y cantidad de comida consumida por el perro

#### Mostrar historial de notificaciones

En esta opción, se mostrará el historial de las diferentes alertas que el sistema emite al usuario.
