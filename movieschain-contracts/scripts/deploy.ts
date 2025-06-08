import { ethers } from "hardhat"

async function main() {
  const AvaliacaoFilme = await ethers.getContractFactory("AvaliacaoFilme")
  const contrato = await AvaliacaoFilme.deploy()

  await contrato.waitForDeployment()

  console.log("Contrato implantado em:", contrato.target)
}

main().catch((error) => {
  console.error(error)
  process.exitCode = 1
})

