<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$conexion=mysqli_connect($hostname,$username,$password,$database);

$AIDI=$_GET["AIDI"];
$nombre=$_GET["Nombre"];
$lugar=$_GET["Lugar"];
$desc=$_GET["Desc"];
$contra=$_GET["Contra"];
$fecha=$_GET["Fecha"];
$hora=$_GET["Hora"];
$propietario=$_GET["Propietario"];
$status=$_GET["Status"];

$sql="UPDATE Clases
    SET 
    clase_nombre='{$nombre}',
    clase_lugar='{$lugar}',
    clase_fecha='{$fecha}',
    clase_hora='{$hora}',
    clase_desc = '{$desc}',
    clase_status='{$status}',
    clase_contra='{$contra}'
    WHERE clase_id = '{$AIDI}'";
$resul=mysqli_query($conexion,$sql);
echo $resul;
mysqli_close($conexion);
?>
