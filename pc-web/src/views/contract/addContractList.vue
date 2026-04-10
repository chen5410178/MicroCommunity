<template>
  <div class="add-contract-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ addContractInfo.parentContractCode ? $t('contract.renewContract') : $t('contract.draftContract')
        }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="_goBack">
          <i class="el-icon-close"></i>{{ $t('common.back') }}
        </el-button>
      </div>

      <el-form ref="form" :model="addContractInfo" label-width="120px" class="text-left" :rules="rules">
        <!-- Parent Contract Info -->
        <el-row v-if="addContractInfo.parentContractCode" :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contract.parentContractName')">
              <el-input v-model="addContractInfo.parentContractName" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.parentContractCode')">
              <el-input v-model="addContractInfo.parentContractCode" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.parentContractStatus')">
              <el-input v-model="addContractInfo.parentStateName" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Basic Contract Info -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contract.contractName')" prop="contractName" required>
              <el-input v-model="addContractInfo.contractName"
                :placeholder="$t('contract.requiredContractName')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.contractCode')" prop="contractCode" required>
              <el-input v-model="addContractInfo.contractCode"
                :placeholder="$t('contract.requiredContractCode')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.contractType')" prop="contractType" required>
              <el-select v-model="addContractInfo.contractType" :placeholder="$t('contract.requiredContractType')"
                style="width:100%" @change="_changeContractType">
                <el-option v-for="item in addContractInfo.contractTypes" :key="item.contractTypeId" :label="item.typeName"
                  :value="item.contractTypeId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Party A Info -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contract.partyA')" prop="partyA" required>
              <el-select v-model="addContractInfo.partyA" :placeholder="$t('contract.requiredPartyA')" style="width:100%"
                @change="_changeContractPartyA">
                <el-option v-for="item in addContractInfo.contractPartyAs" :key="item.partyA" :label="item.partyA"
                  :value="item.partyA">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.partyAContact')" prop="aContacts" required>
              <el-input v-model="addContractInfo.aContacts" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.partyAPhone')" prop="aLink" required>
              <el-input v-model="addContractInfo.aLink" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Party B Info -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contract.partyB')" prop="partyB" required>
              <el-input v-model="addContractInfo.partyB" disabled style="width:70%"></el-input>
              <el-button type="primary" size="small" @click="_searchOwner">{{ $t('contract.select') }}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.partyBContact')" prop="bContacts" required>
              <el-input v-model="addContractInfo.bContacts" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.partyBPhone')" prop="bLink" required>
              <el-input v-model="addContractInfo.bLink" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Operator Info -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contract.operator')" prop="operator" required>
              <el-input v-model="addContractInfo.operator" :placeholder="$t('contract.requiredOperator')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.operatorPhone')" prop="operatorLink" required>
                <el-input v-model="addContractInfo.operatorLink" :placeholder="$t('contract.requiredOperatorPhone')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.contractAmount')">
              <el-input v-model="addContractInfo.amount" :placeholder="$t('contract.requiredContractAmount')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Time Info -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('contract.startTime')" prop="startTime" required>
              <el-date-picker v-model="addContractInfo.startTime" type="date" value-format="yyyy-MM-dd" style="width:100%"
                :placeholder="$t('contract.requiredStartTime')">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.endTime')" prop="endTime" required>
              <el-date-picker v-model="addContractInfo.endTime" type="date" value-format="yyyy-MM-dd" style="width:100%"
                :placeholder="$t('contract.requiredEndTime')">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('contract.signingTime')" prop="signingTime" required>
              <el-date-picker v-model="addContractInfo.signingTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%"
                :placeholder="$t('contract.requiredSigningTime')">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Contract Specs -->
        <div v-for="(item, index) in addContractInfo.contractTypeSpecs" :key="index">
          <el-row :gutter="20" v-if="index % 3 == 0">
            <el-col :span="8" v-for="i in 3" :key="i">
              <el-form-item :label="addContractInfo.contractTypeSpecs[index + i - 1].specName"
                v-if="index + i - 1 < addContractInfo.contractTypeSpecs.length">
                <el-input v-model="addContractInfo.contractTypeSpecs[index + i - 1].value"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- Contract Attachments -->
        <el-form-item :label="$t('contract.contractAttachments')">
          <el-button type="primary" @click="addFileStep">
            <i class="el-icon-plus"></i>{{ $t('contract.addAttachment') }}
          </el-button>
          <div v-for="(item, index) in addContractInfo.contractFilePo" :key="index" style="margin-top:10px">
            <el-row>
              <el-col :span="2">
                <span>{{ $t('contract.attachment') }}{{ index + 1 }}</span>
              </el-col>
              <el-col :span="10">
                <input type="file" @change="getFile($event, index)" accept=".png,.pdf,.jpg">
              </el-col>
              <el-col :span="10">
                <span>{{ item.fileRealName }}</span>
              </el-col>
              <el-col :span="2">
                <el-button type="text" @click="deleteStep(item)">{{ $t('contract.deleteAttachment') }}</el-button>
              </el-col>
            </el-row>
          </div>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Related Rooms -->
    <el-card class="box-card" style="margin-top:20px">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contract.relatedRooms') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="_selectRoom">
          <i class="el-icon-plus"></i>{{ $t('contract.add') }}
        </el-button>
      </div>

      <el-table :data="addContractInfo.rooms" border style="width:100%">
        <el-table-column prop="floorNum" :label="$t('contract.room')" align="center">
          <template slot-scope="scope">
            {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
          </template>
        </el-table-column>
        <el-table-column prop="ownerName" :label="$t('contract.owner')" align="center"></el-table-column>
        <el-table-column prop="link" :label="$t('contract.phone')" align="center"></el-table-column>
        <el-table-column prop="builtUpArea" :label="$t('contract.builtUpArea')" align="center">
          <template slot-scope="scope">
            {{ scope.row.builtUpArea }}{{ $t('contract.squareMeter') }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('contract.roomStatus')" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="_openDelRoomModel(scope.row)">{{ $t('common.delete')
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Approvers -->
    <div v-show="addContractInfo.audit == '1001'">
      <purchase-approvers ref="purchaseApprovers" :flow-type="50005" @notify3="handleNotify"></purchase-approvers>
    </div>

    <!-- Buttons -->
    <div class="text-right" style="margin-top:20px">
      <el-button @click="_goBack" style="margin-right:20px">{{ $t('common.back') }}</el-button>
      <el-button type="primary" @click="saveContractInfo">{{ $t('common.submit') }}</el-button>
    </div>

    <!-- Modals -->
    <search-room ref="searchRoom" @chooseRoom="handleChooseRoom"></search-room>
    <search-owner ref="searchOwner" @chooseOwner="handleChooseOwner"></search-owner>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { saveContract, queryContractType, queryContractPartya, queryContractTypeSpec, queryContractRoom } from '@/api/contract/addContractApi'
import PurchaseApprovers from '@/components/contract/purchaseApprovers'
import SearchRoom from '@/components/room/searchRoom'
import SearchOwner from '@/components/owner/SearchOwner'
import { uploadFile } from '@/api/system/paymentPoolApi'

export default {
  name: 'AddContractList',
  components: {
    PurchaseApprovers,
    SearchRoom,
    SearchOwner
  },
  data() {
    return {
      addContractInfo: {
        contractId: '',
        contractName: '',
        contractCode: '',
        contractType: '',
        partyA: '',
        partyB: '',
        aContacts: '',
        bContacts: '',
        aLink: '',
        bLink: '',
        operator: '',
        operatorLink: '',
        amount: '',
        startTime: '',
        endTime: '',
        signingTime: '',
        contractTypes: [],
        contractTypeSpecs: [],
        objType: '1111',
        objId: '-1',
        contractParentId: '',
        parentContractCode: '',
        parentContractName: '',
        parentStateName: '',
        rooms: [],
        contractFilePo: [],
        tempfile: '',
        contractPartyAs: [],
        audit: '',
        staffName: '',
        nextUserId: ''
      },
      communityId: '',
      rules: {
        contractName: [{ required: true, message: this.$t('contract.requiredContractName'), trigger: 'blur' }],
        contractCode: [{ required: true, message: this.$t('contract.requiredContractCode'), trigger: 'blur' }],
        contractType: [{ required: true, message: this.$t('contract.requiredContractType'), trigger: 'blur' }],
        operator: [{ required: true, message: this.$t('contract.requiredOperator'), trigger: 'blur' }],
        operatorLink: [{ required: true, message: this.$t('contract.requiredOperatorPhone'), trigger: 'blur' }],
        amount: [{ required: true, message: this.$t('contract.requiredContractAmount'), trigger: 'blur' }],
        startTime: [{ required: true, message: this.$t('contract.requiredStartTime'), trigger: 'blur' }],
        endTime: [{ required: true, message: this.$t('contract.requiredEndTime'), trigger: 'blur' }],
        signingTime: [{ required: true, message: this.$t('contract.requiredSigningTime'), trigger: 'blur' }],
        partyA: [{ required: true, message: this.$t('contract.requiredPartyA'), trigger: 'blur' }],
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._loadAddContractType()
    this._loadAddContractParkA()
    if (this.$route.query.contractId) {
      this.addContractInfo.contractParentId = this.$route.query.contractId
      this.addContractInfo.parentContractCode = this.$route.query.contractCode
      this.addContractInfo.parentContractName = this.$route.query.contractName
      this.addContractInfo.parentStateName = this.$route.query.stateName
      this._queryRoom()
      this._listContracts()
    }
  },
  methods: {
    _loadAddContractType() {
      queryContractType({ page: 1, row: 100 }).then(response => {
        this.addContractInfo.contractTypes = response.data
      })
    },
    _loadAddContractParkA() {
      queryContractPartya({ page: 1, row: 100 }).then(response => {
        this.addContractInfo.contractPartyAs = response.data
      })
    },
    _changeContractType() {
      this.addContractInfo.contractTypes.forEach(item => {
        if (this.addContractInfo.contractType == item.contractTypeId) {
          this.addContractInfo.audit = item.audit
        }
      })

      this.addContractInfo.contractTypeSpecs = []
      queryContractTypeSpec({
        page: 1,
        row: 100,
        contractTypeId: this.addContractInfo.contractType
      }).then(response => {
        response.data.forEach(item => {
          item.value = ''
          if (item.specShow == 'Y') {
            item.values = []
            this.addContractInfo.contractTypeSpecs.push(item)
          }
        })
      })
    },
    _queryRoom() {
      queryContractRoom({
        contractId: this.$route.query.contractId,
        page: 1,
        row: 100
      }).then(response => {
        this.addContractInfo.rooms = response.data
      })
    },
    _selectRoom() {
      this.$refs.searchRoom.open()
    },
    _searchOwner() {
      this.$refs.searchOwner.open()
    },
    handleChooseRoom(room) {
      this.addContractInfo.rooms.push(room)
    },
    handleChooseOwner(owner) {
      this.addContractInfo.partyB = owner.name
      this.addContractInfo.bContacts = owner.name
      this.addContractInfo.bLink = owner.link
      this.addContractInfo.objId = owner.ownerId
    },
    handleNotify(info) {
      this.addContractInfo.nextUserId = info.staffId
      this.addContractInfo.staffName = info.staffName
    },
    _openDelRoomModel(room) {
      this.addContractInfo.rooms = this.addContractInfo.rooms.filter(item => item.roomId != room.roomId)
    },
    addFileStep() {
      this.addContractInfo.contractFilePo.push({
        seq: this.addContractInfo.contractFilePo.length,
        fileSaveName: '',
        fileRealName: ''
      })
    },
    deleteStep(step) {
      this.addContractInfo.contractFilePo = this.addContractInfo.contractFilePo.filter(item => item.seq != step.seq)
    },
    getFile(e, index) {
      this.addContractInfo.tempfile = e.target.files[0]
      this.addContractInfo.contractFilePo[index].fileRealName = this.addContractInfo.tempfile.name
      this._importData(index)
    },
   async _importData(index) {
      console.log(index)
      const _fileName = this.addContractInfo.tempfile.name
      const _suffix = _fileName.substring(_fileName.lastIndexOf('.') + 1)
      if (!this.checkFileType(_suffix.toLowerCase())) {
        this.$message.error(this.$t('contract.invalidFileType'))
        return
      }

      const formData = new FormData()
      formData.append("uploadFile", this.addContractInfo.tempfile)
      const  data  = await uploadFile(formData, {})
      this.addContractInfo.contractFilePo[index].fileSaveName = data.realFileName
      // 这里需要实现文件上传逻辑
      // uploadContactFile(formData).then(response => {
      //   this.addContractInfo.contractFilePo[index].fileSaveName = response.data
      //   this.$message.success(this.$t('common.operationSuccess'))
      // })
    },
    checkFileType(fileType) {
      return ['png', 'pdf', 'jpg'].includes(fileType)
    },
    _changeContractPartyA() {
      const partyA = this.addContractInfo.partyA
      this.addContractInfo.contractPartyAs.forEach(item => {
        if (partyA == item.partyA) {
          this.addContractInfo.aLink = item.aLink
          this.addContractInfo.aContacts = item.aContacts
        }
      })
    },
    _listContracts() {
      // 需要实现查询合同详情逻辑
    },
    saveContractInfo() {
      if (!this.validateForm()) {
        return
      }

      this.addContractInfo.communityId = this.communityId
      saveContract(this.addContractInfo).then(response => {
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(response.msg)
        }
      })
    },
    validateForm() {
      // 实现表单验证逻辑
      return true
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.add-contract-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.text-right {
  text-align: right;
}
</style>