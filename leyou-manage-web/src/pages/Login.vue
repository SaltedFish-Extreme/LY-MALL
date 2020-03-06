<template>
  <v-app>
    <v-content>
      <v-container fill-height fluid>
        <v-layout align-center justify-center>
          <v-flex md4 sm8 xs12>
            <v-card class="elevation-12">
              <v-toolbar color="primary" dark>
                <v-toolbar-title>乐优商城后台管理</v-toolbar-title>
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field label="用户名" prepend-icon="person" type="text" v-model="username"/>
                  <v-text-field
                    :append-icon="e1 ? 'visibility' : 'visibility_off'"
                    :append-icon-cb="() => (e1 = !e1)"
                    :type="e1 ? 'text' : 'password'"
                    id="password"
                    label="密码"
                    prepend-icon="lock"
                    v-model="password"
                  ></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="doLogin" color="primary">登录</v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
    <v-dialog v-model="dialog" width="300px">
      <v-alert :value="true" color="error" icon="warning">
        用户名和密码不能为空
      </v-alert>
    </v-dialog>
  </v-app>
</template>

<script>
  export default {
    data: () => ({
      username: "",
      password: "",
      dialog: false,
      e1: false
    }),
    methods: {
      doLogin() {
        if (!this.username || !this.password) {
          this.dialog = true;
          return false;
        }
        console.log(this.username + " ... " + this.password);
        this.$router.push("/");
      }
    }
  };
</script>
