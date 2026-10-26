from pathlib import Path
import subprocess,tempfile,zipfile,time,urllib.request,urllib.parse,urllib.error,os,shutil
root=Path(__file__).resolve().parents[1]
repo=Path.home()/'.m2/repository'
config=root/'.mvn/maven.config'
if config.exists():
 for line in config.read_text().splitlines():
  if line.startswith('-Dmaven.repo.local='): repo=Path(line.split('=',1)[1])
h2=repo/'com/h2database/h2/2.3.232/h2-2.3.232.jar'
java=str(Path(os.environ['JAVA_HOME'])/'bin/java') if os.environ.get('JAVA_HOME') else shutil.which('java')
if not java or not h2.exists() or not (root/'target/Tarea3Servlet.war').exists():
 raise SystemExit('Primero instala Java 21 y compila el proyecto con Maven.')
class NoRedirect(urllib.request.HTTPRedirectHandler):
 def redirect_request(self,*args): return None
opener=urllib.request.build_opener(NoRedirect)
def request(path,data=None):
 try:
  r=opener.open(urllib.parse.urljoin('http://localhost:18083',path), None if data is None else urllib.parse.urlencode(data).encode(),timeout=10)
 except urllib.error.HTTPError as e: r=e
 return r.status,r.headers,r.read().decode()
def datos():
 d=dict(fecha='2026-10-18',moneda='PEN',monto='1000',periodo='12',cuota='100',tea='12',vencimiento='2027-10-18')
 for i in range(10): d[f'pagos[{i}].id']=str(i+1);d[f'pagos[{i}].pago']=''
 return d
with tempfile.TemporaryDirectory(prefix='tarea3-http-') as td:
 p=Path(td)
 shutil.copyfile(root/'target/Tarea3Servlet.war',p/'test.war')
 with zipfile.ZipFile(p/'test.war','a',compression=zipfile.ZIP_STORED) as z:
  z.write(h2,'WEB-INF/lib/h2-test.jar')
 log=open(p/'app.log','w+')
 proc=subprocess.Popen([java,'-jar',str(p/'test.war'),'--server.port=18083','--spring.datasource.url=jdbc:h2:mem:http;MODE=MySQL;DB_CLOSE_DELAY=-1','--spring.datasource.driver-class-name=org.h2.Driver','--spring.datasource.username=sa','--spring.datasource.password='],cwd=root,stdout=log,stderr=subprocess.STDOUT)
 try:
  for _ in range(60):
   try:
    status,_,body=request('/credito');break
   except (OSError,urllib.error.URLError):time.sleep(.25)
  assert status==200,(status,body[:200]);assert body.count('aria-label="Pago de')==10
  d=datos();d['pagos[5].pago']='2670';d['pagos[7].pago']='150';d['pagos[0].tarjeta']='FALSIFICADA';d['pagos[0].gastos']='999'
  status,headers,body=request('/credito',d);assert status==302,(status,body[:500])
  location=headers['Location'];status,_,body=request(location);assert status==200 and '2670.00' in body and '150.00' in body and 'FALSIFICADA' not in body
  assert request(location)[0]==200
  for valor in ['-1','1.234','abc','10000000000000']:
   d=datos();d['pagos[0].pago']=valor
   status,_,body=request('/credito',d);assert status==400,(valor,status);assert 'Peluquería' in body and 'role="alert"' in body
  for modo in ['duplicado','desconocido','incompleto','malformado']:
   d=datos()
   if modo=='duplicado':d['pagos[1].id']='1'
   elif modo=='desconocido':d['pagos[1].id']='999'
   elif modo=='malformado':d['pagos[1].id']='abc'
   else:d.pop('pagos[9].id');d.pop('pagos[9].pago')
   assert request('/credito',d)[0]==400,modo
  status,_,body=request('/credito');assert '2670.00' in body
  print('HTTP: formulario, resultado, redirección, recarga, pagos persistidos, campos protegidos y 8 entradas inválidas: OK.')
 finally:
  proc.terminate();proc.wait(timeout=15)
  if 'status' not in locals() or status!=200:
   log.seek(0);print(log.read()[-4000:])
  log.close()
