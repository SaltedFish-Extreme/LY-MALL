<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="params"
      class="elevation-0"
      hide-actions
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center">{{ formatBoolean(props.item.numeric) }}</td>
        <td class="text-xs-center">{{ props.item.unit || '无' }}</td>
        <td class="text-xs-center">{{ formatBoolean(props.item.generic) }}</td>
        <td class="text-xs-center">{{ formatBoolean(props.item.searching) }}</td>
        <td class="justify-center layout px-0">
          <v-btn @click="editParam(props.item)" icon>
            <i class="el-icon-edit"/>
          </v-btn>
          <v-btn @click="deleteParam(props.item.id)" icon>
            <i class="el-icon-delete"/>
          </v-btn>
        </td>
      </template>
      <template slot="no-data">
        该分组下没有参数
      </template>
    </v-data-table>
    <v-btn @click="addParam" color='primary'>新增参数</v-btn>
    <v-dialog max-width="350px" scrollable v-model="show">
      <v-card>
        <v-card-text style="height: 300px;">
          <v-flex class="px-3">
            <v-text-field label="参数名称：" v-model="param.name"/>
            <v-checkbox color="primary" hide-details label="是否为通用属性" v-model="param.generic"/>
            <v-checkbox color="primary" hide-details label="是否为数值类型" v-model="param.numeric"/>
            <v-text-field label="数值单位：" v-if="param.numeric" v-model="param.unit"/>
            <v-checkbox color="primary" hide-details label="是否用于搜索" v-model="param.searching"/>
            <v-flex class="px-2" v-if="param.searching && param.numeric">
              搜索过滤区间：
              <v-layout :key="i" row v-for="(s,i) in param.segments" wrap>
                <v-flex xs5>
                  <v-text-field hide-details prefix="From: " single-line v-model="s[0]"/>
                </v-flex>
                <v-spacer/>
                <v-flex xs5>
                  <v-text-field hide-details prefix="To: " single-line v-model="s[1]"/>
                </v-flex>
                <v-flex xs1>
                  <v-btn @click="param.segments.splice(i,1)" icon>
                    <i class="el-icon-delete"/>
                  </v-btn>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-spacer/>
                <v-flex xs1>
                  <v-tooltip left>
                    <v-btn @click="param.segments.push([0,0])" icon slot="activator">
                      <v-icon>add</v-icon>
                    </v-btn>
                    <span>点击为数值类型的参数添加分段，便于搜索过滤</span>
                  </v-tooltip>
                </v-flex>
              </v-layout>
            </v-flex>
          </v-flex>
        </v-card-text>
        <v-card-actions>
          <v-spacer/>
          <v-btn @click.native="show=false" color="blue darken-1" flat>取消</v-btn>
          <v-btn @click.native="save" color="blue darken-1" flat>保存</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
  export default {
    name: "v-spec-param",
    props: {
      group: {
        type: Object
      }
    },
    data() {
      return {
        headers: [
          {text: "id", value: "id", align: "center", sortable: false},
          {text: "参数名", value: "name", align: "center", sortable: false},
          {
            text: "是否为数值", value: "numeric",
            align: "center",
            sortable: false
          },
          {text: "单位", value: "unit", align: "center", sortable: false},
          {
            text: "是否通用",
            value: "generic",
            align: "center",
            sortable: false
          },
          {
            text: "是否可搜索",
            value: "searching",
            align: "center",
            sortable: false
          },
          {text: "操作", align: "center", sortable: false}
        ],
        params: [], // 参数
        show: false,
        param: {},
        isEdit: false
      };
    },
    watch: {
      group: {
        deep: true,
        handler(val) {
          if (val && val.id) {
            this.loadData();
          }
        }
      }
    },
    methods: {
      loadData() {
        this.$http
          .get("/item/spec/params?gid=" + this.group.id)
          .then(({data}) => {
            data.forEach(p => {
              p.segments = p.segments ? p.segments.split(",").map(s => s.split("-")) : [];
            })
            this.params = data;
          })
          .catch(() => {
            this.params = [];
          });
      },
      editParam(param) {
        this.param = param;
        this.isEdit = true;
        this.show = true;
      },
      addParam() {
        this.param = {
          cid: this.group.cid,
          groupId: this.group.id,
          segments: [],
          numeric: false,
          searching: false,
          generic: false
        }
        this.show = true;
      },
      deleteParam(id) {
        this.$message.confirm("确认要删除该参数吗？")
          .then(() => {
            this.$http.delete("/item/spec/param/" + id)
              .then(() => {
                this.$message.success("删除成功");
              })
              .catch(() => {
                this.$message.error("删除失败");
              })
          })
      },
      formatBoolean(boo) {
        return boo ? "是" : "否";
      },
      save() {
        const p = {};
        Object.assign(p, this.param);
        p.segments = p.segments.map(s => s.join("-")).join(",")
        this.$http({
          method: this.isEdit ? 'put' : 'post',
          url: '/item/spec/param',
          data: p,
        }).then(() => {
          // 关闭窗口
          this.show = false;
          this.$message.success("保存成功！");
          this.loadData();
        }).catch(() => {
          this.$message.error("保存失败！");
        });
      }
    }
  };
</script>
