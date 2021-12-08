<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";

$conexion=mysqli_connect($hostname,$username,$password,$database);

$dato=false;

if(isset($_GET['USER']) && isset($_GET['RESULT']) && (isset($_GET['TODAY']) && isset($_GET['BEFORE5DAYS'])) && isset($_GET['TYPE'])){
    $usuario=$_GET["USER"]; 
    $result=$_GET["RESULT"];
    $descrip=$_GET["DESCRIPT"];
    $date_now=$_GET["TODAY"];
    $date_before=$_GET["BEFORE5DAYS"];
    
    if($_GET['TYPE'] == "Alumno"){
        $sql_existAlert="SELECT alerta_id FROM `Alertas` WHERE alerta_usuario = '{$usuario}' AND alerta_fecha BETWEEN '{$date_before}' AND '{$date_now}'";
    
        $result_existAlert = mysqli_query($conexion, $sql_existAlert);
        
        if(empty(mysqli_fetch_array($result_existAlert))){
            $sql_clasesUser="SELECT DISTINCT Clases.clase_id FROM Usuarios JOIN Asistencias ON Usuarios.usuario_id = Asistencias.asistencia_usuario
    			 		   JOIN Clases ON Asistencias.asistencia_clase = Clases.clase_id
             WHERE Usuarios.usuario_id = '{$usuario}'
             ORDER BY Clases.clase_id ASC";
        
            $result_clasesUser = mysqli_query($conexion, $sql_clasesUser);
            
            if($result_clasesUser){
                while($reg_clasesUser = mysqli_fetch_array($result_clasesUser)){
                    $sql_alert="INSERT INTO `Alertas`(`alerta_clase`, `alerta_usuario`, `alerta_positivo`, `alerta_sintomas`, `alerta_fecha`) VALUES 
                                ('".$reg_clasesUser[0]."','".$usuario."','".$result."','".$descrip."','".$date_now."')";
                    $result_alert = mysqli_query($conexion, $sql_alert);
                    if($result_alert)
                        $dato = true;
                }
            }
        }
    }else{
        $sql_existAlert="SELECT alerta_id FROM `Alertas` WHERE alerta_usuario = '{$usuario}' AND alerta_fecha BETWEEN '{$date_before}' AND '{$date_now}'";
    
        $result_existAlert = mysqli_query($conexion, $sql_existAlert);
        
        if(empty(mysqli_fetch_array($result_existAlert))){
            $sql_clasesUser="SELECT DISTINCT Clases.clase_id FROM Usuarios JOIN Clases ON Usuarios.usuario_id = Clases.clase_propietario
             WHERE Usuarios.usuario_id = '{$usuario}'
             ORDER BY Clases.clase_id ASC";
        
            $result_clasesUser = mysqli_query($conexion, $sql_clasesUser);
            
            if($result_clasesUser){
                while($reg_clasesUser = mysqli_fetch_array($result_clasesUser)){
                    $sql_alert="INSERT INTO `Alertas`(`alerta_clase`, `alerta_usuario`, `alerta_positivo`, `alerta_sintomas`, `alerta_fecha`) VALUES 
                                ('".$reg_clasesUser[0]."','".$usuario."','".$result."','".$descrip."','".$date_now."')";
                    $result_alert = mysqli_query($conexion, $sql_alert);
                    if($result_alert)
                        $dato = true;
                }
            }
        }
    }
}

$result = array();
$result["dato"] = 1;

if($dato){
    $result["dato"] = 0;
    echo json_encode($result);
}else
    echo json_encode($result);

mysqli_close($conexion);

?>