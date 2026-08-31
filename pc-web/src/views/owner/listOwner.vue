<template>
  <div class="list-owner-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <el-card class="role-card">
          <ul class="role-list">
            <li v-for="(item, index) in personRoles" :key="index" class="role-item"
              :class="{ 'role-item-active': conditions.personRole === item.statusCd }" @click="switchPersonRole(item)">
              {{ item.name }}
            </li>
          </ul>
        </el-card>
      </el-col>

      <el-col :span="21">
        <el-card class="search-card">
          <div slot="header" class="text-left">
            <span>{{ $t('listOwner.searchTitle') }}</span>
          </div>
          <el-form :model="conditions" label-width="0">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="conditions.personType" :placeholder="$t('listOwner.personTypeName')"
                    style="width:100%">
                    <el-option v-for="(type, index) in personTypes" :key="index" :label="type.label"
                      :value="type.value" />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.name" :placeholder="$t('listOwner.placeholders.name')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.roomName" :placeholder="$t('listOwner.placeholders.room')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.link" :placeholder="$t('listOwner.placeholders.phone')" type="number" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.idCard" :placeholder="$t('listOwner.placeholders.idCard')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-button type="primary" @click="queryOwnerMethod">
                    {{ $t('listOwner.buttons.search') }}
                  </el-button>
                  <el-button @click="resetOwnerMethod">
                    {{ $t('listOwner.buttons.reset') }}
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>

        <el-card class="owner-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('listOwner.ownerInfo') }}</span>
            <el-button type="primary" size="small" class="float-right" @click="openAddOwnerModal">
              {{ $t('listOwner.buttons.add') }}
            </el-button>
          </div>

          <el-table :data="owners" border v-loading="loading" style="width:100%">
            <el-table-column :label="$t('listOwner.columns.face')" width="80" align="center">
              <template slot-scope="scope">
                <el-image style="width:60px;height:60px;border-radius:5px;cursor:pointer;"
                  :src="scope.row.faceUrl || '/img/noPhoto.jpg'" :preview-src-list="[scope.row.faceUrl]" fit="cover" />
              </template>
            </el-table-column>

            <el-table-column :label="$t('listOwner.columns.name')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.name }}</div>
                <div v-if="scope.row.personType === 'C'">{{ scope.row.concactPerson }}</div>
              </template>
            </el-table-column>

            <el-table-column prop="personTypeName" :label="$t('listOwner.columns.type')" align="center" />

            <el-table-column prop="personRoleName" :label="$t('listOwner.columns.role')" align="center" />

            <el-table-column :label="$t('listOwner.columns.gender')" align="center">
              <template slot-scope="scope">
                {{ scope.row.sex == 0 ? $t('listOwner.gender.male') : $t('listOwner.gender.female') }}
              </template>
            </el-table-column>

            <el-table-column prop="idCard" :label="$t('listOwner.columns.idCard')" align="center">
              <template slot-scope="scope">
                {{ scope.row.idCard || '-' }}
              </template>
            </el-table-column>

            <el-table-column prop="link" :label="$t('listOwner.columns.phone')" align="center" />

            <el-table-column prop="concactLink" :label="$t('listOwner.columns.backupPhone')" align="center">
              <template slot-scope="scope">
                {{ scope.row.concactLink || '-' }}
              </template>
            </el-table-column>

            <el-table-column prop="address" :label="$t('listOwner.columns.address')" align="center">
              <template slot-scope="scope">
                {{ scope.row.address || '-' }}
              </template>
            </el-table-column>

            <el-table-column :label="$t('listOwner.columns.rooms')" align="center">
              <template slot-scope="scope">
                <el-link type="primary" @click="viewOwnerRooms(scope.row)">
                  {{ scope.row.roomCount || 0 }}
                </el-link>
              </template>
            </el-table-column>

            <el-table-column :label="$t('listOwner.columns.members')" align="center">
              <template slot-scope="scope">
                <el-link v-if="scope.row.ownerTypeCd === '1001'" type="primary" @click="viewOwnerMembers(scope.row)">
                  {{ scope.row.memberCount || 0 }}
                </el-link>
                <span v-else>-</span>
              </template>
            </el-table-column>

            <el-table-column :label="$t('listOwner.columns.cars')" align="center">
              <template slot-scope="scope">
                <el-link type="primary" @click="viewOwnerCars(scope.row)">
                  {{ scope.row.carCount || 0 }}
                </el-link>
              </template>
            </el-table-column>

            <el-table-column :label="$t('listOwner.columns.debts')" align="center">
              <template slot-scope="scope">
                <el-link type="primary" @click="viewOweFees(scope.row)">
                  {{ scope.row.oweFee || '0.00' }}
                </el-link>
              </template>
            </el-table-column>

            <el-table-column v-for="(item, index) in listColumns" :key="index" :label="item" align="center">
              <template slot-scope="scope">
                {{ scope.row.listValues[index] || '-' }}
              </template>
            </el-table-column>

            <el-table-column :label="$t('listOwner.columns.operation')" width="300" align="center" fixed="right">
              <template slot-scope="scope">
                <el-dropdown>
                  <el-button type="primary" size="mini">
                    {{ $t('listOwner.buttons.edit') }}
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="openEditOwnerModal(scope.row)">
                      {{ $t('listOwner.actions.editOwner') }}
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="openDeleteOwnerModal(scope.row)">
                      {{ $t('listOwner.actions.deleteOwner') }}
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="openAddOwnerRoom(scope.row)" v-if="scope.row.ownerTypeCd === '1001'">
                      {{ $t('listOwner.actions.addRoom') }}
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="openDeleteOwnerRoom(scope.row)" v-if="scope.row.ownerTypeCd === '1001'">
                      {{ $t('listOwner.buttons.unbindRoom') }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>

                <el-button v-if="scope.row.ownerTypeCd === '1001'" type="info" size="mini"
                  @click="openOwnerDetailMember(scope.row)">
                  {{ $t('listOwner.buttons.member') }}
                </el-button>

                <el-button type="info" size="mini" @click="openOwnerDetail(scope.row)">
                  {{ $t('listOwner.buttons.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize"
            :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handlePageChange" />
        </el-card>
      </el-col>
    </el-row>

    <add-owner ref="addOwner" @success="handleOwnerSuccess" />
    <edit-owner ref="editOwner" @success="handleOwnerSuccess" />
    <delete-owner ref="deleteOwner" @success="handleOwnerSuccess" />
    <owner-rooms ref="ownerRooms" />
    <owner-members ref="ownerMembers" />
    <owner-cars ref="ownerCars" />
    <owner-owe-fees ref="ownerOweFees" />

  </div>
</template>

<script>
import { queryOwners } from '@/api/owner/ownerApi'
import AddOwner from '@/components/owner/addOwner'
import EditOwner from '@/components/owner/editOwner'
import DeleteOwner from '@/components/owner/deleteOwner'
import { getAttrSpecList } from '@/api/dev/attrSpecApi'
import OwnerRooms from '@/components/owner/ownerRooms'
import OwnerMembers from '@/components/owner/ownerMembers'
import OwnerCars from '@/components/owner/ownerCars'
import OwnerOweFees from '@/components/owner/ownerOweFees'


export default {
  name: 'ListOwner',
  components: {
    AddOwner,
    EditOwner,
    DeleteOwner,
    OwnerRooms,
    OwnerMembers,
    OwnerCars,
    OwnerOweFees
  },
  data() {
    return {
      loading: false,
      owners: [],
      personRoles: [],
      personTypes: [
        { value: '', label: this.$t('common.select') },
        { value: 'P', label: this.$t('listOwner.personType.personal') },
        { value: 'C', label: this.$t('listOwner.personType.company') }
      ],
      conditions: {
        personRole: '',
        name: '',
        roomName: '',
        link: '',
        idCard: '',
        personType: ''
      },
      listColumns: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.getPersonRoles()
    this.getColumns()
    this.listOwnerData()
  },
  methods: {
    async getPersonRoles() {
      this.personRoles = [
        { statusCd: '', name: this.$t('listOwner.role.all') },
        { statusCd: '1', name: this.$t('listOwner.role.owner') },
        { statusCd: '2', name: this.$t('listOwner.role.tenant') }
      ]
    },

    async getColumns() {
      try {
        const { data } = await getAttrSpecList({
          page: 1,
          row: 100,
          tableName: 'building_owner_attr'
        })
        this.listColumns = data
          .filter(item => item.specShow === 'Y')
          .map(item => item.specName)
      } catch (error) {
        console.error('Failed to get columns:', error)
      }
    },

    async listOwnerData() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.currentPage,
          row: this.pageSize
        }
        const { data, total } = await queryOwners(params)
        this.owners = data
        this.total = total
        this.dealOwnerAttr(data)
      } catch (error) {
        console.error('Failed to get owner list:', error)
        this.$message.error(this.$t('listOwner.fetchError'))
      } finally {
        this.loading = false
      }
    },

    dealOwnerAttr(owners) {
      owners.forEach(owner => {
        owner.listValues = []
        if (!owner.ownerAttrDtos || owner.ownerAttrDtos.length === 0) return

        this.listColumns.forEach(column => {
          const attr = owner.ownerAttrDtos.find(dto => dto.specName === column)
          owner.listValues.push(attr ? attr.valueName : '')
        })
      })
    },

    switchPersonRole(item) {
      this.conditions.personRole = item.statusCd
      this.listOwnerData()
    },

    queryOwnerMethod() {
      this.currentPage = 1
      this.listOwnerData()
    },

    resetOwnerMethod() {
      this.conditions = {
        personRole: '',
        name: '',
        roomName: '',
        link: '',
        idCard: '',
        personType: ''
      }
      this.listOwnerData()
    },

    handleSizeChange(size) {
      this.pageSize = size
      this.listOwnerData()
    },

    handlePageChange(page) {
      this.currentPage = page
      this.listOwnerData()
    },

    openAddOwnerModal() {
      this.$refs.addOwner.open()
    },

    openEditOwnerModal(owner) {
      this.$refs.editOwner.open(owner)
    },

    openDeleteOwnerModal(owner) {
      this.$refs.deleteOwner.open(owner)
    },

    handleOwnerSuccess() {
      this.listOwnerData()
    },

    viewOwnerRooms(owner) {
      console.log('View owner rooms:', owner)
      // 实际项目中跳转到房屋页面
      this.$refs.ownerRooms.open(owner)

    },

    viewOwnerMembers(owner) {
      console.log('View owner members:', owner)
      // 实际项目中跳转到成员页面
      this.$refs.ownerMembers.open(owner.ownerId)

    },

    viewOwnerCars(owner) {
      console.log('View owner cars:', owner)
      // 实际项目中跳转到车辆页面
      this.$refs.ownerCars.open(owner.ownerId)

    },

    viewOweFees(owner) {
      console.log('View owe fees:', owner)
      // 实际项目中跳转到欠费页面
      this.$refs.ownerOweFees.open(owner.ownerId)

    },

    openAddOwnerRoom(owner) {
      console.log('Add owner room:', owner)
      // 实际项目中跳转到绑定房屋页面
      this.$router.push(`/views/owner/ownerBindRoom?ownerId=${owner.ownerId}`)

    },

    openDeleteOwnerRoom(owner) {
      console.log('Delete owner room:', owner)
      // 实际项目中跳转到解绑房屋页面
      this.$router.push(`/views/owner/deleteOwnerRoom?ownerId=${owner.ownerId}`)

    },

    openOwnerDetail(owner) {
      console.log('Owner detail:', owner)
      // 实际项目中跳转到详情页面
      this.$router.push(`/views/owner/ownerDetail?ownerId=${owner.ownerId}&needBack=Y`)

    },

    openOwnerDetailMember(owner) {
      console.log('Owner detail member:', owner)
      // 实际项目中跳转到成员详情页面
      this.$router.push(`/views/owner/ownerDetail?ownerId=${owner.ownerId}`)

    }
  }
}
</script>

<style scoped>
.list-owner-container {
  padding: 20px;
}

.role-card {
  height: 100%;
}

.role-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.role-item {
  padding: 10px;
  text-align: center;
  cursor: pointer;
  border-radius: 4px;
  margin-bottom: 5px;
  transition: all 0.3s;
}

.role-item:hover {
  background-color: #f5f7fa;
}

.role-item-active {
  background-color: #409eff;
  color: white;
}

.search-card,
.owner-card {
  margin-bottom: 20px;
}

.float-right {
  float: right;
}

.el-dropdown {
  margin-right: 10px;
}
</style>