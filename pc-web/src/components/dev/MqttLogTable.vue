<template>
    <el-card>
      <div slot="header">
        <span>{{ $t('mqttLog.list.title') }}</span>
      </div>
      <el-table
        v-loading="loading"
        :data="data"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="mqttId"
          :label="$t('mqttLog.table.id')"
          align="center"
        />
        <el-table-column
          prop="businessId"
          :label="$t('mqttLog.table.businessId')"
          align="center"
        />
        <el-table-column
          prop="businessKey"
          :label="$t('mqttLog.table.business')"
          align="center"
        />
        <el-table-column
          prop="topic"
          :label="$t('mqttLog.table.topic')"
          align="center"
        />
        <el-table-column
          prop="context"
          :label="$t('mqttLog.table.content')"
          align="center"
        >
          <template slot-scope="scope">
            <el-tooltip :content="scope.row.context" placement="top">
              <div class="text-ellipsis">{{ scope.row.context }}</div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column
          prop="remark"
          :label="$t('mqttLog.table.remark')"
          align="center"
        />
      </el-table>
    </el-card>
  </template>
  
  <script>
  export default {
    name: 'MqttLogTable',
    props: {
      data: {
        type: Array,
        default: () => []
      },
      loading: {
        type: Boolean,
        default: false
      }
    }
  }
  </script>
  
  <style scoped>
  .text-ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 200px;
  }
  </style>
