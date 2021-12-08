<?PHP
$hostname="localhost";
$database="id17651994_damdb";
$username="id17651994_administrador";
$password="Password12345!";

$clases_user = array();
$result_user = array();

$flag = false;

$conexion=mysqli_connect($hostname,$username,$password,$database);

if(isset($_GET['User'])&&isset($_GET['Compa'])){
    $User=$_GET['User'];
    $Compa=$_GET['Compa'];
    
    $query_compa = 
        "SELECT DISTINCT Clases.clase_id
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
                            WHERE Usuarios.usuario_id = '{$Compa}')
            ORDER BY Clases.clase_id";
    $result_compa = mysqli_query($conexion,$query_compa);
    
    $query_user = 
        "SELECT DISTINCT Clases.clase_id, Usuarios.usuario_id, Usuarios.usuario_nombres, Usuarios.usuario_apellidos,
                         Clases.clase_nombre, Clases.clase_lugar,
                         Clases.clase_fecha, Clases.clase_hora, Clases.clase_desc,
                         Clases.clase_status
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
                            WHERE Usuarios.usuario_id = '{$User}')
            ORDER BY Clases.clase_id";
    $result_user = mysqli_query($conexion,$query_user);
    
    if ($result_compa){
        if($result_user){
            $i = 0;
            while($reg_user = mysqli_fetch_array($result_user)){
                $result_compa -> data_seek(0);
                while($reg_compa = mysqli_fetch_array($result_compa)){
                    if ($reg_user[0] == $reg_compa[0]){
                        $clases_user['datos'][$i] = $reg_user;
                        $i++;
                        break;
                    }
                }
            }
        }else
            $flag = true;
    }else
        $flag = true;
    
    if($flag){
        $result_user["usuario_id"] = '';
        $result_user["usuario_nombres"] = '';
        $result_user["usuario_apellidos"] = '';
        $result_user["clase_id"] = '';
        $result_user["clase_nombre"] = '';
        $result_user["clase_lugar"] = '';
        $result_user["clase_fecha"] = '';
        $result_user["clase_hora"] = '';
        $result_user["clase_desc"] = '';
        $result_user["clase_status"] = '';
        $clases_user['datos'][] = $result_user;
    }
}else{
    $result_user["usuario_id"] = '';
    $result_user["usuario_nombres"] = '';
    $result_user["usuario_apellidos"] = '';
    $result_user["clase_id"] = '';
    $result_user["clase_nombre"] = '';
    $result_user["clase_lugar"] = '';
    $result_user["clase_fecha"] = '';
    $result_user["clase_hora"] = '';
    $result_user["clase_desc"] = '';
    $result_user["clase_status"] = '';
    $clases_user['datos'][] = $result_user;
}

mysqli_close($conexion); 
echo json_encode($clases_user);
?>