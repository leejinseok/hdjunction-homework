from locust import task, FastHttpUser, stats

stats.PERCENTILES_TO_CHART = [0.95, 0.99]

class LoadTest(FastHttpUser):
    connection_timeout = 10.0
    network_timeout = 10.0

    @task
    def hello(self):
        self.client.get("/hello")

    @task
    def getPatients(self):
        self.client.get("/api/v1/patients?pageNo=0&pageSize=10")

    @task
    def getPatient(self):
        self.client.get("/api/v1/patients/1")