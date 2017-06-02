$wshell = New-Object -ComObject wscript.shell;
$wshell.AppActivate('num lock key press to prevent screen lock')

$continueLoop = $true

while ($continueLoop){
	$wshell.SendKeys('{NUMLOCK}')

    #sleep 20 sec
	Sleep 20
}