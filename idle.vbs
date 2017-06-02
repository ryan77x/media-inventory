Dim objResult

Set objShell = WScript.CreateObject("WScript.Shell")    
i = 0

Do While i = 0
  objResult = objShell.sendkeys("{NUMLOCK}")
'  objResult = objShell.sendkeys("{SCROLLLOCK}")
  Wscript.Sleep (20000)
Loop