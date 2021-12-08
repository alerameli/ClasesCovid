<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";

$IDUser = array();
$Infected = array();

$UserDetected = false;
$Abort = false;

$UserInfected = 0;

$conexion=mysqli_connect($hostname,$username,$password,$database);

if(isset($_GET['TODAY']) && isset($_GET['BEFORE5DAYS']) && isset($_GET['IDUsuario'])){
    $TODAY = $_GET['TODAY'];
    $BEFORE5DAYS = $_GET['BEFORE5DAYS'];
    $IDUsuario = $_GET['IDUsuario'];
    
    $query_UserInfected="SELECT DISTINCT Usuarios.usuario_id AS 'UserInfected', Alertas.alerta_clase AS 'ClasesInfected'
            FROM Usuarios
                JOIN Alertas
                ON Alertas.alerta_usuario=Usuarios.usuario_id
                WHERE Alertas.alerta_positivo='positivo'
                AND Alertas.alerta_fecha BETWEEN '{$BEFORE5DAYS}' AND '{$TODAY}';";
    $result_UserInfected = mysqli_query($conexion,$query_UserInfected);
    
    $query_UserPassed = "SELECT DISTINCT Clases.clase_id
            FROM Clases JOIN Usuarios 
            ON Clases.clase_propietario = Usuarios.usuario_id
            AND Clases.clase_propietario = '{$IDUsuario}'";
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
                        
                        if(!$UserDetected){
                            $i = 0;
                            while($i < sizeof($IDUser) && !$UserDetected){
                                $UserInf = $IDUser[$i];
                                if($UserInf == $reg_Infected[0])
                                    $UserDetected = true;
                                
                                $i++;
                            }
                        }
                        
                        if(!$UserDetected){
                            $IDUser[$UserInfected] = $reg_Infected[0];
                            $UserInfected = $UserInfected + 1;
                        }
                    
                        $UserDetected = false;
                        
                    }
                }
            }
            
        }else
            $Abort = true;
    }else
        $Abort = true;
}else
    $Abort = true;


if($Abort)
    $Infected['datos'][0] = '0';
else
    $Infected['datos'][0] = $UserInfected;


echo json_encode($Infected);
mysqli_close($conexion);
?>