<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";

$conexion=mysqli_connect($hostname,$username,$password,$database);

$dato=false;
$result = array();
$result["dato"] = 1;

if(isset($_GET['USER']) && (isset($_GET['TODAY']) && isset($_GET['BEFORE5DAYS']))){
    $usuario=$_GET["USER"]; 
    $date_now=$_GET["TODAY"];
    $date_before=$_GET["BEFORE5DAYS"];
    
    $sql_ImInfected="SELECT alerta_id FROM `Alertas` WHERE alerta_usuario = '{$usuario}' AND alerta_fecha BETWEEN '{$date_before}' AND '{$date_now}'";
    
    $result_ImInfected = mysqli_query($conexion, $sql_ImInfected);
    
    if(!empty(mysqli_fetch_array($result_ImInfected))){
        $result["dato"] = 0;
    }
}

echo json_encode($result);
mysqli_close($conexion);

?>