<?php
$query_string = "";
if ($_POST) {
  $kv = array();
  foreach ($_POST as $key => $value) {
    $kv[] = "$key=$value";
  }
  $query_string = join("&", $kv);
}
else {
  $query_string = $_SERVER['QUERY_STRING'];
}
file_put_contents("/tmp/vsearchd_post.log", $query_string  . "\n" ,FILE_APPEND);
?>

