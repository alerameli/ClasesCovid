<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";

$conexion=mysqli_connect($hostname,$username,$password,$database);

$result = array();
$result["dato"] = 1;

if(isset($_GET['USER'])){
    $usuario=$_GET["USER"];
    
    $sql_deleteAlert="DELETE FROM `Alertas` WHERE alerta_usuario = '{$usuario}'";
    
    $result_deleteAlert = mysqli_query($conexion, $sql_deleteAlert);
    
}

if($result_deleteAlert)
    $result["dato"] = 0;

echo json_encode($result);

mysqli_close($conexion);

?>