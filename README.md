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

### Crear rutina

Para crear una rutina, se debe ingresar el horario y la cantidad de comida.

### Cargar perfil por defecto

Si se selecciona esta opción, el usuario tendrá un perfil de perro por defecto y se recomendará una rutina por defecto.

### Menú principal

En el menú principal tendremos las siguientes opciones:

#### Mostrar rutina actual

En esta opción, se mostrará la rutina de comidas que actualmente se le está dando al perro.

#### Modificar rutina

En esta opción, el usuario podrá cambiar el horario o cantidad de comida de la rutina.


#### Cambiar perfil

En esta opción, se podrán cambiar los datos del perfil del perro.

#### Alimentación manual

En esta opción, el usuario solo con pulsar un botón en la app podrá alimentar al perro en el momento, ingresando la cantidad de comida a soltar.

#### Mostrar historial de comidas

En esta opción, se mostrará el historial con fecha y hora y cantidad de comida de cada comida del perro

#### Recarga de comida en depósito

Cuando el depósito esté con poca comida se activará una luz LED para que sea recargado.

#### Alerta de comida rápida

Si el perro está comiendo muy rápido, se activará una alerta sonora para que deje de hacerlo.

#### Alerta de cantidad de comida
El sistema notificara alertas en la app,  si el perro comió mucho o poco en el día.

