<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";
$conexion=mysqli_connect($hostname,$username,$password,$database);

$nombre=$_GET["Nombre"];
$lugar=$_GET["Lugar"];
$desc=$_GET["Desc"];
$contra=$_GET["Contra"];
$fecha=$_GET["Fecha"];
$hora=$_GET["Hora"];
$propietario=$_GET["Propietario"];
$status=$_GET["Status"];

$sql="insert into Clases
        (clase_nombre,
        clase_lugar,
        clase_desc,
        clase_contra,
        clase_fecha,
        clase_hora,
        clase_propietario,
        clase_status) 
    values('$nombre','$lugar','$desc','$contra','$fecha','$hora','$propietario','$status')";
$resul=mysqli_query($conexion,$sql);
echo $resul;
mysqli_close($conexion);
?>
