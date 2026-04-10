<template>
  <el-dialog
    :title="$t('addOwnerCommittee.addTitle')"
    :visible.sync="visible"
    width="80%"
    @close="resetForm"
  >
    <el-form ref="form" :model="formData" label-width="120px" :rules="rules">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.name')" prop="name">
            <el-input
              v-model="formData.name"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.name')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.sex')" prop="sex">
            <el-select
              v-model="formData.sex"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.sex')"
              style="width:100%"
              clearable
            >
              <el-option :label="$t('addOwnerCommittee.male')" value="B" />
              <el-option :label="$t('addOwnerCommittee.female')" value="G" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.phone')" prop="link">
            <el-input
              v-model="formData.link"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.phone')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.idCard')" prop="idCard">
            <el-input
              v-model="formData.idCard"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.idCard')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.address')" prop="address">
            <el-input
              v-model="formData.address"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.address')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.position')" prop="position">
            <el-input
              v-model="formData.position"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.position')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.post')" prop="post">
            <el-input
              v-model="formData.post"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.post')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.postDesc')">
            <el-input
              v-model="formData.postDesc"
              :placeholder="$t('addOwnerCommittee.optional') + $t('addOwnerCommittee.postDesc')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.appointTime')" prop="appointTime">
            <el-input
              v-model="formData.appointTime"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.appointTime')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.curTime')" prop="curTime">
            <el-input
              v-model="formData.curTime"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.curTime')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.state')" prop="state">
            <el-select
              v-model="formData.state"
              :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.state')"
              style="width:100%"
            >
              <el-option :label="$t('addOwnerCommittee.inService')" value="1000" />
              <el-option :label="$t('addOwnerCommittee.leave')" value="2000" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerCommittee.remark')">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :placeholder="$t('addOwnerCommittee.optional') + $t('addOwnerCommittee.remark')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-card class="box-card" style="margin-top:20px">
        <div slot="header" class="clearfix">
          <span>{{ $t('addOwnerCommittee.emergencyContact') }}</span>
          <el-button type="primary" size="mini" style="float: right; padding: 3px 0" @click="addContract">
            {{ $t('addOwnerCommittee.add') }}
          </el-button>
        </div>
        <el-table :data="formData.contracts" border style="width: 100%">
          <el-table-column :label="$t('addOwnerCommittee.contactRelation')" align="center">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.relName"
                :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.contactRelation')"
              />
            </template>
          </el-table-column>
          <el-table-column :label="$t('addOwnerCommittee.contactName')" align="center">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.name"
                :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.contactName')"
              />
            </template>
          </el-table-column>
          <el-table-column :label="$t('addOwnerCommittee.contactPhone')" align="center">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.link"
                :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.contactPhone')"
              />
            </template>
          </el-table-column>
          <el-table-column :label="$t('addOwnerCommittee.contactAddress')" align="center" width="250">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.address"
                :placeholder="$t('addOwnerCommittee.required') + $t('addOwnerCommittee.contactAddress')"
              />
            </template>
          </el-table-column>
          <el-table-column :label="$t('addOwnerCommittee.operation')" align="center" width="100">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" @click="deleteContract(scope.row)">
                {{ $t('addOwnerCommittee.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('addOwnerCommittee.back') }}</el-button>
      <el-button type="primary" @click="saveOwnerCommitteeInfo">{{ $t('addOwnerCommittee.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOwnerCommittee } from '@/api/owner/addOwnerCommitteeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddOwnerCommittee',
  data() {
    return {
      visible: false,
      formData: {
        name: '',
        sex: '',
        link: '',
        idCard: '',
        address: '',
        position: '',
        post: '',
        postDesc: '',
        appointTime: '',
        curTime: '',
        state: '',
        remark: '',
        contracts: []
      },
      rules: {
        name: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.name'), trigger: 'blur' },
          { max: 30, message: this.$t('addOwnerCommittee.name') + this.$t('addOwnerCommittee.maxLength30'), trigger: 'blur' }
        ],
        sex: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.sex'), trigger: 'change' }
        ],
        link: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.phone'), trigger: 'blur' },
          { max: 11, message: this.$t('addOwnerCommittee.phone') + this.$t('addOwnerCommittee.maxLength11'), trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.idCard'), trigger: 'blur' },
          { max: 18, message: this.$t('addOwnerCommittee.idCard') + this.$t('addOwnerCommittee.maxLength18'), trigger: 'blur' }
        ],
        address: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.address'), trigger: 'blur' },
          { max: 256, message: this.$t('addOwnerCommittee.address') + this.$t('addOwnerCommittee.maxLength256'), trigger: 'blur' }
        ],
        position: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.position'), trigger: 'blur' },
          { max: 64, message: this.$t('addOwnerCommittee.position') + this.$t('addOwnerCommittee.maxLength64'), trigger: 'blur' }
        ],
        post: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.post'), trigger: 'blur' },
          { max: 64, message: this.$t('addOwnerCommittee.post') + this.$t('addOwnerCommittee.maxLength64'), trigger: 'blur' }
        ],
        postDesc: [
          { max: 64, message: this.$t('addOwnerCommittee.postDesc') + this.$t('addOwnerCommittee.maxLength64'), trigger: 'blur' }
        ],
        appointTime: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.appointTime'), trigger: 'blur' },
          { max: 256, message: this.$t('addOwnerCommittee.appointTime') + this.$t('addOwnerCommittee.maxLength256'), trigger: 'blur' }
        ],
        curTime: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.curTime'), trigger: 'blur' },
          { max: 256, message: this.$t('addOwnerCommittee.curTime') + this.$t('addOwnerCommittee.maxLength256'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('addOwnerCommittee.required') + this.$t('addOwnerCommittee.state'), trigger: 'change' }
        ],
        remark: [
          { max: 200, message: this.$t('addOwnerCommittee.remark') + this.$t('addOwnerCommittee.maxLength200'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.formData = {
        name: '',
        sex: '',
        link: '',
        idCard: '',
        address: '',
        position: '',
        post: '',
        postDesc: '',
        appointTime: '',
        curTime: '',
        state: '',
        remark: '',
        contracts: []
      }
    },
    addContract() {
      this.formData.contracts.push({
        id: this.generateUUID(),
        relName: '',
        name: '',
        link: '',
        address: ''
      })
    },
    deleteContract(contract) {
      const index = this.formData.contracts.findIndex(item => item.id === contract.id)
      if (index !== -1) {
        this.formData.contracts.splice(index, 1)
      }
    },
    generateUUID() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0
        const v = c === 'x' ? r : (r & 0x3 | 0x8)
        return v.toString(16)
      })
    },
    async saveOwnerCommitteeInfo() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const communityId = await getCommunityId()
            const data = {
              ...this.formData,
              communityId
            }
            await saveOwnerCommittee(data)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('common.saveFailed'))
          }
        }
      })
    }
  }
}
</script>