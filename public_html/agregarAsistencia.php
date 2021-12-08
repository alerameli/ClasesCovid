<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";

$conexion=mysqli_connect($hostname,$username,$password,$database);

$clase=$_GET["clase"];
$usuario=$_GET["usuario"];

$sql="insert into Asistencias(asistencia_clase,asistencia_usuario) values('$clase','$usuario')";
$resul=mysqli_query($conexion,$sql);
echo $resul;
mysqli_close($conexion);
?>