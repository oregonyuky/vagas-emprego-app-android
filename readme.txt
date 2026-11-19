VAGAS ONLINE - INSTRUÇÕES DE EXECUÇÃO
=====================================

O projeto contém:
  backend/  - API REST Java Spring Boot, MongoDB e frontend web administrativo.
  android/  - Aplicativo Android nativo que consome a API com Retrofit.


1. PRÉ-REQUISITOS
-----------------

- Java JDK 17 ou mais recente (JAVA_HOME configurado).
- MongoDB 8 instalado OU Docker Desktop.
- Para o app: Android Studio com Android SDK 37 e um emulador Android.
- Internet na primeira compilação para baixar dependências Maven/Gradle.


2. INICIAR O MONGODB
--------------------

Opção A - Docker (execute na raiz do projeto):

  docker compose up -d

Opção B - MongoDB instalado no Windows:

  Inicie o serviço "MongoDB" pelo painel Serviços do Windows, ou execute o
  mongod apontando para uma pasta de dados. A porta esperada é 27017.

A API usa por padrão:

  mongodb://localhost:27017/vagas_online

Para usar outra conexão, defina a variável MONGODB_URI antes de iniciar a API.

Na inicialização, a API carrega automaticamente e sem duplicar os arquivos em
backend/src/main/resources/data nas coleções cargos, empresas, vagas e
interesses. A carga contém 31 cargos, 19 empresas, 10 vagas e 1 interesse.

Os dados enviados por quem toca em "Tenho interesse" ficam no banco
vagas_online, coleção interesses. Cada documento guarda a vaga e os dados do
candidato (nome, CPF, e-mail, telefone e, quando informada, formação). Como os
endpoints definidos no trabalho não incluem consulta de interesses, visualize-os
pelo MongoDB
Compass conectando em mongodb://localhost:27017 e abrindo:

  vagas_online > interesses

Ou pelo mongosh:

  mongosh mongodb://localhost:27017/vagas_online
  db.interesses.find().pretty()


3. INICIAR A API E O FRONTEND WEB
---------------------------------

No PowerShell:

  cd backend
  .\mvnw.cmd spring-boot:run

No Linux/macOS:

  cd backend
  ./mvnw spring-boot:run

Quando aparecer "Started VagasOnlineApplication", acesse:

  Painel web:  http://localhost:8080/
  Teste da API: http://localhost:8080/apis/vagas/get-all

Na primeira execução são carregados no MongoDB os JSONs fornecidos no trabalho.
O painel web permite criar, editar e excluir vagas.

Para gerar o JAR:

  .\mvnw.cmd clean package
  java -jar target\vagas-online-backend-1.0.0.jar


4. EXECUTAR O APLICATIVO ANDROID
--------------------------------

1) Mantenha o MongoDB e a API em execução.
2) Abra a pasta "android" no Android Studio.
3) Aguarde o Gradle Sync terminar.
4) Crie/inicie um emulador e clique em Run.

O endereço padrão da API no app é:

  http://10.0.2.2:8080/

10.0.2.2 é o endereço do computador hospedeiro visto pelo emulador oficial.
Para usar um celular físico, altere api_base_url em:

  android/app/src/main/res/values/strings.xml

Troque 10.0.2.2 pelo IPv4 do computador na rede local, por exemplo:

  http://192.168.0.10:8080/

O celular e o computador devem estar na mesma rede, e a porta 8080 deve estar
liberada no firewall.

Se o firewall bloquear o emulador e você não puder liberar a porta, uma opção
de desenvolvimento é executar "adb reverse tcp:8080 tcp:8080" e trocar
temporariamente api_base_url para http://127.0.0.1:8080/.

Também é possível compilar pelo PowerShell:

  cd android
  .\gradlew.bat assembleDebug

APK gerado:

  android\app\build\outputs\apk\debug\app-debug.apk


5. FUNCIONALIDADES IMPLEMENTADAS
--------------------------------

- Busca de empresas e cargos.
- Criação, consulta, edição e exclusão de vagas.
- Pesquisa de vagas por cidade e estado no aplicativo Android.
- Dados da vaga: empresa, cargo, cidade, estado, requisitos, formação,
  conhecimentos, regime (CLT/PJ/Estágio), jornada e remuneração.
- Painel web administrativo responsivo.
- Tabela administrativa com todos os detalhes e busca textual de vagas.
- Pesquisa e detalhes de vagas no Android.
- Ação "Tenho interesse" no Android com nome, CPF, e-mail e telefone.
- Persistência de vagas e interesses no MongoDB.
- Validação dos dados e respostas HTTP adequadas.


6. PRINCIPAIS ENDPOINTS
-----------------------

POST   /apis/vagas/vaga
PUT    /apis/vagas
DELETE /apis/vagas/{registro}
GET    /apis/vagas/get-all
GET    /apis/vagas/get-one/{registro}
GET    /apis/empresa/get-all-empresas
GET    /apis/cargo/get-all-cargos
POST   /apis/interesse/candidato


7. ENCERRAR
-----------

Encerre a API com Ctrl+C. Se usou Docker, na raiz do projeto execute:

  docker compose down

Os dados permanecem no volume mongodb-data. Para removê-los também, use
"docker compose down -v" somente se realmente quiser apagar o banco.
