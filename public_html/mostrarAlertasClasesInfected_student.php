<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";

$IDUser = array();
$IDClase = array();
$Infected = array();

$UserDetected = false;
$ClaseDetected = false;
$Abort = false;

$UserInfected = 0;
$ClaseInfected = 0;

$conexion=mysqli_connect($hostname,$username,$password,$database);

if(isset($_GET['TODAY']) && isset($_GET['BEFORE5DAYS']) && isset($_GET['IDUsuario'])){
    $TODAY = $_GET['TODAY'];
    $BEFORE5DAYS = $_GET['BEFORE5DAYS'];
    $IDUsuario = $_GET['IDUsuario'];
    
    $query_UserInfected="SELECT DISTINCT Usuarios.usuario_id AS 'UserInfected', Asistencias.asistencia_clase AS 'ClasesInfected'
            FROM Alertas JOIN Asistencias JOIN Usuarios
            WHERE 
                Alertas.alerta_usuario = Asistencias.asistencia_usuario AND 
                Alertas.alerta_usuario = Usuarios.usuario_id AND 
                Alertas.alerta_fecha BETWEEN '{$BEFORE5DAYS}' AND '{$TODAY}';";
    $result_UserInfected = mysqli_query($conexion,$query_UserInfected);
    
    $query_UserPassed = "SELECT DISTINCT Clases.clase_id
            FROM Clases JOIN Usuarios 
            ON Clases.clase_propietario = Usuarios.usuario_id
            JOIN Asistencias
            ON Clases.clase_id = Asistencias.asistencia_clase
            WHERE 
            	Clases.clase_status = 'concluida' AND 
            	Clases.clase_id IN (SELECT Clases.clase_id
                            FROM Clases
                            JOIN Asistencias
                            JOIN Usuarios
                            ON Asistencias.asistencia_usuario = Usuarios.usuario_id 
                            AND Clases.clase_id = Asistencias.asistencia_clase
                            WHERE Usuarios.usuario_id = '{$IDUsuario}')";
    $result_UserPassed = mysqli_query($conexion,$query_UserPassed);
    
    if($result_UserInfected){
        if($result_UserPassed){
            while($reg_Passed=mysqli_fetch_array($result_UserPassed)){
                $result_UserInfected -> data_seek(0);
                while($reg_Infected=mysqli_fetch_array($result_UserInfected)){
                    
                    if($reg_Passed[0] == $reg_Infected[1]){
                        if(empty($IDUser)){
                            $IDUser[$UserInfected] = $reg_Infected[0];
                            $UserInfected = $UserInfected + 1;
                            $UserDetected = true;
                        }
                        
                        if(empty($IDClase)){
                            $IDClase[$ClaseInfected] = $reg_Infected[1];
                            $ClaseInfected = $ClaseInfected + 1;
                            $ClaseDetected = true;
                        }
                        
                        if(!$UserDetected){
                            $i = 0;
                            while($i < sizeof($IDUser) && !$UserDetected){
                                $UserInf = $IDUser[$i];
                                if($UserInf == $reg_Infected[0])
                                    $UserDetected = true;
                                
                                $i++;
                            }
                        }
                        
                        if(!$ClaseDetected){
                            $i = 0;
                            while($i < sizeof($IDClase) && !$ClaseDetected){
                                $ClaseInf = $IDClase[$i];
                                if($ClaseInf == $reg_Infected[1])
                                    $ClaseDetected = true;
                                
                                $i++;
                            }
                        }
                        
                        if(!$UserDetected){
                            $IDUser[$UserInfected] = $reg_Infected[0];
                            $UserInfected = $UserInfected + 1;
                        }
                        
                        if(!$ClaseDetected){
                            $IDClase[$ClaseInfected] = $reg_Infected[1];
                            $ClaseInfected = $ClaseInfected + 1;
                        }
                    
                        $UserDetected = false;
                        $ClaseDetected = false;
                        
                    }
                }
            }
            
            $result = array();
            $result[0] = $UserInfected;
            $result[1] = $ClaseInfected;
            $Infected['datos'][] = $result;
            echo json_encode($Infected);
        }else
            $Abort = true;
    }else
        $Abort = true;
}else
    $Abort = true;


if($Abort){
    $result = array();
    $result[0] = '0';
    $result[1] = '0';
    $Infected['datos'][] = $result;
    echo json_encode($Infected);
    
}

mysqli_close($conexion);
?>